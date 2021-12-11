package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.AllArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class SetLocationCommand implements CommandExecutor {

    private final ConfigContainer configContainer;
    private final TeamContainer teamContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Config locationsFile = configContainer.getFile("locations");

        if (!player.hasPermission("bedwars.command.setlocation")) {
            player.sendMessage("No permission!");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage("Not enough arguments!");
            player.sendMessage("Usage: /setlocation [spawn, end, spectator]");
            player.sendMessage("Usage: /setlocation [name of team] [spawn, bed]");
            return false;
        }

        switch (args[0]) {
            case "spawn":
                locationsFile.getFileConfiguration().set("spawn", player.getLocation());
                player.sendMessage("Spawn Location was set.");
                break;
            case "end":
                locationsFile.getFileConfiguration().set("end", player.getLocation());
                player.sendMessage("End Location was set.");
                break;
            case "spectator":
                locationsFile.getFileConfiguration().set("spectator", player.getLocation());
                player.sendMessage("Spectator Location was set.");
                break;
        }

        Team team = teamContainer.getTeamByName(args[0]);

        if (team != null) {
            switch (args[1]) {
                case "spawn":
                    team.setSpawnLocation(player.getLocation());
                    player.sendMessage("Spawn location for team + " + team.getName() + " + was set.");
                    break;
                case "bed":
                    for (Block block : player.getLineOfSight(null, 10)) {
                        if (block.getType().name().contains("BED")) {
                            player.sendMessage("Bed location for team + " + team.getName() + " + was set.");
                            team.setBedLocation(block.getLocation());
                        }
                    }
                    break;
            }
        }

        teamContainer.save();
        locationsFile.save();
        return true;
    }
}