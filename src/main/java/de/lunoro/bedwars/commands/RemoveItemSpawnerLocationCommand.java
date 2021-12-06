package de.lunoro.bedwars.commands;

import de.lunoro.bedwars.game.spawner.ItemSpawner;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class RemoveItemSpawnerLocationCommand implements CommandExecutor {

    private final ItemSpawnerContainer itemSpawnerContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Fired command");
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.removeitemspawnerlocation")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Nicht genügend Argumente!");
            player.sendMessage("Usage: /removeItemSpawnerLocation [material]");
            return false;
        }

        Material material = Material.getMaterial(args[0]);

        if (material == null) {
            player.sendMessage("Item nicht gefunden!");
            return false;
        }

        ItemSpawner itemSpawner = itemSpawnerContainer.getItemSpawner(material.name());

        if (itemSpawner == null) {
            player.sendMessage("Keinen ItemSpawner mit diesem item gefunden!");
            return false;
        }

        itemSpawnerContainer.removeNearestItemSpawnerLocation(itemSpawner, player.getLocation());
        player.sendMessage("ItemSpawner wurde entfernt.");
        return true;
    }
}