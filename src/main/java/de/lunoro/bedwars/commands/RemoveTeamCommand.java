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
        System.out.println("Fired command");
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.removeteam")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Nicht genügend Argumente!");
            player.sendMessage("Usage: /removeTeam [name]");
            return false;
        }

        Team team = teamContainer.getTeamByName(args[0]);

        if (team == null) {
            player.sendMessage("Kein Team mit diesem namen gefunden!");
            return false;
        }

        teamContainer.removeTeam(team);
        return true;
    }
}
