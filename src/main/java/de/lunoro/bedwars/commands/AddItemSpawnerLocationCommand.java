package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.spawner.ItemSpawner;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class AddItemSpawnerLocationCommand implements CommandExecutor {

    private final ItemSpawnerContainer itemSpawnerContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Fired command");
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.setitemspawnerlocation")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (args.length != 2) {
            player.sendMessage("Nicht genügend Argumente!");
            player.sendMessage("Usage: /additemspawnerlocation [material]");
            return false;
        }

        ItemSpawner itemSpawner = itemSpawnerContainer.getItemSpawner(args[0]);

        if (itemSpawner == null) {
            player.sendMessage("ItemSpawner nicht gefunden.");
            return false;
        }

        itemSpawner.getLocationList().add(player.getLocation());
        player.sendMessage("Neue location wurde hinzugefügt.");
        return true;
    }
}