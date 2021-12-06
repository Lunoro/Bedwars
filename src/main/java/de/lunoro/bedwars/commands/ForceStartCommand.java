package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ForceStartCommand implements CommandExecutor {

    private final Game game;
    private final boolean isStartedInBuildingMode;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Fired command");
        if (!(sender instanceof Player)) {
            sender.sendMessage("Du bist kein Spieler!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.forcestart")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (!game.getGamePhase().equals(GamePhase.START)) {
            player.sendMessage("Das kann jetzt nicht benutzt werden!");
            return false;
        }

        if (isStartedInBuildingMode) {
            player.sendMessage("Der Server befindet sich im Baumodus es wird also kein Spiel gestartet werden.");
            return false;
        }

        game.forceStart();
        player.sendMessage("Spiel wurde gestartet.");
        return true;
    }
}