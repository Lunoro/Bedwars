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
public class CreateItemSpawnerCommand implements CommandExecutor {

    private final ItemSpawnerContainer itemSpawnerContainer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Fired command");
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.command.createitemspawner")) {
            player.sendMessage("Dafür hast du keine Berechtigung!");
            return false;
        }

        if (args.length != 2) {
            player.sendMessage("Nicht genügend Argumente!");
            player.sendMessage("Usage: /createItemSpawner [material] [dropDuration]");
            return false;
        }

        if (!isNumeric(args[1])) {
            player.sendMessage("Das ist keine Zahl!");
            return false;
        }

        Material material = Material.getMaterial(args[0]);
        double dropDuration = Integer.parseInt(args[1]);

        if (material == null) {
            player.sendMessage("Item nicht gefunden!");
            return false;
        }

        ItemSpawner itemSpawner = new ItemSpawner(material, dropDuration);
        itemSpawnerContainer.add(itemSpawner);
        player.sendMessage("ItemSpawner erstellt.");
        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}