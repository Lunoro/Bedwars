package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.Game;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class ForceStartCommand implements CommandExecutor {

    private Game game;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Du bist kein Spieler.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.forcestart")) {
            player.sendMessage("Dafür hast du keine Berechtigung.");
            return false;
        }

        game.forceStart();
        player.sendMessage("Forcestart");
        return true;
    }
}
