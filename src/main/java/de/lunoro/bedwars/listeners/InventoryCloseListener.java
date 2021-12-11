package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.shopinventory.ShopInventory;
import de.lunoro.bedwars.shopinventory.ShopInventoryRegistry;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@AllArgsConstructor
public class InventoryCloseListener implements Listener {

    private final ShopInventoryRegistry shopInventoryRegistry;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        ShopInventory shopInventory = shopInventoryRegistry.getInventory(player);

        if (!event.getInventory().equals(shopInventory.getInventory())) {
            return;
        }

        shopInventoryRegistry.deleteInventory(player);
    }
}
