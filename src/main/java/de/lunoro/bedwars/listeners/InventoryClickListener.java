package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.builder.ItemBuilder;
import de.lunoro.bedwars.shopinventory.ShopInventory;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class InventoryClickListener implements Listener {

    private final ShopInventory shopInventory;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory shopGui = shopInventory.getInventory();
        if (!event.getClickedInventory().equals(shopGui)) {
            return;
        }

        ItemNode itemNode = shopInventory.getItemNode(event.getSlot());

        if (itemNode == null) {
            return;
        }

        if (itemNode.getPrice() == 0) {
            shopInventory.setChildItemsFromItemNode(itemNode, shopGui);
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (player.getInventory().contains(itemNode.getPriceMaterial(), itemNode.getPrice())) {
            player.sendMessage("Du hast zu wenig Items um das zu kaufen.");
            player.closeInventory();
            event.setCancelled(true);
            return;
        }

        ItemStack itemsToRemove = new ItemBuilder(itemNode.getItem()).setAmount(itemNode.getPrice()).toItemStack();
        player.getInventory().removeItem(itemsToRemove);
        player.getInventory().addItem(itemNode.getItem());
        event.setCancelled(true);
    }
}
