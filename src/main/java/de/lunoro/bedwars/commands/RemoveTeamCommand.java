package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class RemoveTeamCommand implements CommandExecutor {

    private final TeamContainer teamContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.removeteam")) {
            player.sendMessage("No permission!");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Not enough arguments!");
            player.sendMessage("Usage: /removeTeam [name]");
            return false;
        }

        Team team = teamContainer.getTeamByName(args[0]);

        if (team == null) {
            player.sendMessage("No team found with this name!");
            return false;
        }

        teamContainer.removeTeam(team);
        return true;
    }
}
