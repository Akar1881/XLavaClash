package com.xlavaclash.managers;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.models.Queue;
import com.xlavaclash.models.Team;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {
    private final XLavaClash plugin;
    private final Map<String, Queue> queues;

    public QueueManager(XLavaClash plugin) {
        this.plugin = plugin;
        this.queues = new ConcurrentHashMap<>();
    }

    public Queue createQueue(String mapName) {
        return plugin.getMapManager().getMap(mapName)
            .map(map -> {
                Queue queue = new Queue(plugin, map);
                queues.put(mapName, queue);
                return queue;
            })
            .orElse(null);
    }

    public int getPlayersInQueue(String mapName) {
        Queue queue = queues.get(mapName);
        return queue != null ? queue.getPlayers().size() : 0;
    }

    public int getTeamQueueCount(String mapName, Team team) {
        Queue queue = queues.get(mapName);
        if (queue == null) return 0;
        return (int) queue.getPlayers().values().stream()
            .filter(t -> t == team)
            .count();
    }

    public void removeQueue(String mapName) {
        queues.remove(mapName);
    }

    public boolean addPlayer(Player player, String mapName, Team team) {
        Queue queue = queues.get(mapName);
        if (queue == null) {
            queue = createQueue(mapName);
            if (queue == null) {
                return false;
            }
        }
        
        if (team != null) {
            return queue.addPlayer(player, team);
        } else {
            return queue.addPlayer(player);
        }
    }

    public void removeAllPlayers() {
        queues.values().forEach(Queue::clear);
        queues.clear();
    }

    public void removePlayer(Player player) {
        for (Queue queue : queues.values()) {
            queue.removePlayer(player);
        }
    }

    public int getPlayersInQueue() {
        return queues.values().stream()
            .mapToInt(queue -> queue.getPlayers().size())
            .sum();
    }
}