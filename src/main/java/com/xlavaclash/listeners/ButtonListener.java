package com.xlavaclash.listeners;

import com.xlavaclash.XLavaClash;
import com.xlavaclash.cooldown.ButtonCooldown;
import com.xlavaclash.utils.ButtonWand;
import com.xlavaclash.items.ItemRegistry;
import com.xlavaclash.models.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ButtonListener implements Listener {
    private final XLavaClash plugin;
    private final ButtonCooldown cooldown;

    public ButtonListener(XLavaClash plugin) {
        this.plugin = plugin;
        this.cooldown = new ButtonCooldown(plugin.getConfig().getLong("button-cooldown", 1000));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (ButtonWand.isButtonWand(item) && block.getType() == Material.STONE_BUTTON) {
            event.setCancelled(true);
            plugin.getMapManager().getAllMaps().values().stream()
                .filter(map -> map.getWorldName().equals(block.getWorld().getName()))
                .findFirst()
                .ifPresent(map -> {
                    map.setButtonLocation(block.getLocation());
                    plugin.getMapManager().saveMap(map);
                    player.sendMessage("§aButton location set for map '" + map.getName() + "'!");
                });
        }
    }

    @EventHandler
    public void onButtonClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.STONE_BUTTON) return;

        Player player = event.getPlayer();
        Block button = event.getClickedBlock();

        if (cooldown.isOnCooldown(player.getUniqueId())) {
            long remainingTime = cooldown.getRemainingTime(player.getUniqueId());
            String message = plugin.getConfig().getString("messages.button-cooldown", "&cYou must wait %time% seconds before using the button again!")
                .replace("%time%", String.format("%.1f", remainingTime / 1000.0))
                .replace('&', '§');
            player.sendMessage(message);
            event.setCancelled(true);
            return;
        }

        plugin.getGameManager().getActiveGames().values().stream()
            .filter(game -> game.getPlayers().stream()
                .anyMatch(gp -> gp.getPlayer().equals(player)))
            .findFirst()
            .ifPresent(game -> {
                if (game.getState() != GameState.PLAYING) {
                    player.sendMessage("§cButtons can only be used during active games!");
                    return;
                }

                if (game.getMap().getButtonLocation() != null &&
                    game.getMap().getButtonLocation().equals(button.getLocation())) {
                    
                    cooldown.setCooldown(player.getUniqueId());
                    
                    Object item = ItemRegistry.getRandomItem();
                    if (item instanceof ItemStack[]) {
                        for (ItemStack stack : (ItemStack[]) item) {
                            player.getInventory().addItem(stack);
                        }
                    } else {
                        player.getInventory().addItem((ItemStack) item);
                    }
                    player.sendMessage("§aYou received a random item!");
                }
            });
    }
}