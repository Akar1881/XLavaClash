# XLavaClash

A Minecraft plugin that creates an exciting PvP experience where two teams battle on a lava-divided island, featuring a unique button-based item system and comprehensive player progression.

## Features

- Team-based PvP gameplay (RED vs BLUE)
- Multiple team sizes (Solo, Duo, Trio, Squad)
- Dynamic item spawning system using buttons
- Player ranking and XP system
- Comprehensive statistics tracking
- Queue system with team selection
- Admin spectator mode
- Custom scoreboard display

## Commands

### Player Commands
- `/xlc join [mapname]` - Join a game queue
- `/xlc leave` - Leave the current queue
- `/xlc lobby` - Teleport to the lobby
- `/xlc rank` - View your rank information
- `/xlc help` - Show help message

### Admin Commands
- `/xlc create <mapname> <worldname>` - Create a new map
- `/xlc delete <mapname>` - Delete a map
- `/xlc setqueue <mapname>` - Set queue lobby location
- `/xlc setbutton <mapname>` - Set button location
- `/xlc setteam <mapname> <red/blue>` - Set team spawn location
- `/xlc setsize <mapname> <solo/duo/trio/squad>` - Set team size
- `/xlc map list` - List all maps
- `/xlc map info <mapname>` - Show map info
- `/xlc map active <mapname>` - Activate a map
- `/xlc map deactive <mapname>` - Deactivate a map
- `/xlc spec join <mapname>` - Spectate a map
- `/xlc spec quit` - Stop spectating

## Map Design Guidelines

Each map should be created in a void world and contain the following elements:

1. Queue Area
   - A designated spawn point for players in queue
   - Can be any design but should be separate from the main arena
   - Set using `/xlc setqueue <mapname>`

2. Main Arena
   - Circular island design
   - Located in the center of the map
   - Split into two equal halves by a line of lava running through the center

3. Button Setup
   - A bedrock block in the center of the island
   - Stone button placed on the bedrock block
   - Set using `/xlc setbutton <mapname>`

4. Team Spawns
   - RED team spawn on one side of the lava
   - BLUE team spawn on the opposite side
   - Set using `/xlc setteam <mapname> <red/blue>`

Example Layout:
```
                Queue Area
                    ↓
                [Lobby]
                   |
                   |
     RED Spawn     |     BLUE Spawn
         ↓         |         ↓
      [===|========B========|===]
          |        ↑        |
          |     Button      |
          |                 |
          |‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|
          LLLLLLLLLLLLLLLLL
          (Lava Divider)
```

## Installation

1. Download the latest beta
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin using in-game commands

## Dependencies

- Spigot/Paper 1.17+
- WorldEdit

## Permissions

- `xlavaclash.admin` - Access to admin commands
- `xlavaclash.play` - Access to player commands (default: true)

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a new branch for your feature
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## Support

If you encounter any issues or have suggestions, please open an issue on GitHub.