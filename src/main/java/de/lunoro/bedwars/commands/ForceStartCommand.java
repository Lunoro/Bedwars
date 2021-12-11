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
        if (!(sender instanceof Player)) {
            sender.sendMessage("You're not a player!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.forcestart")) {
            player.sendMessage("No permission!");
            return false;
        }

        if (!game.getGamePhase().equals(GamePhase.START)) {
            player.sendMessage("You can't use that now!");
            return false;
        }

        if (isStartedInBuildingMode) {
            player.sendMessage("The server is in building mode so no game will be started.");
            return false;
        }

        game.forceStart();
        player.sendMessage("Game was started.");
        return true;
    }
}