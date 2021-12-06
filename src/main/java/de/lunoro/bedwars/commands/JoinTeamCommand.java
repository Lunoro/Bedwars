package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import de.lunoro.bedwars.game.team.Team;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class JoinTeamCommand implements CommandExecutor {

    private final Game game;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Du bist kein Spieler!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.jointeam")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (!game.getGamePhase().equals(GamePhase.START)) {
            player.sendMessage("Das kann jetzt nicht benutzt werden!");
            return false;
        }

        Team teamToJoin = game.getTeamContainer().getTeamByName(args[0]);

        if (teamToJoin == null) {
            player.sendMessage("Team nicht gefunden!");
            return false;
        }

        if (teamToJoin.getTeamMember(player) != null) {
            player.sendMessage("Du befindest dich bereits in diesem Team.");
            return false;
        }

        Team teamPlayerIsCurrentlyIn = game.getTeamContainer().getTeamOfPlayer(player);

        if (teamPlayerIsCurrentlyIn != null) {
            teamPlayerIsCurrentlyIn.removeTeamMember(player);
        }

        teamToJoin.addTeamMember(player);
        player.sendMessage("Du bist " + args[0] + " beigetreten.");
        return true;
    }
}
