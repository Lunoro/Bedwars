package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CreateTeamCommand implements CommandExecutor {

    private TeamContainer teamContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.createteam")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (args.length <= 2) {
            player.sendMessage("Nicht genügend Argumente!");
            player.sendMessage("Usage: /createTeam [name] [colorCode (&c - red)]");
            return false;
        }

        teamContainer.addTeam(new Team(args[0], args[1].charAt(1)));
        return true;
    }
}
