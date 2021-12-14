package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.builder.ItemBuilder;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.shopinventory.ShopInventory;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class InventoryClickListener implements Listener {

    private final Game game;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ShopInventory shopInventory = game.getShopInventoryRegistry().getInventory(player);

        if (!event.getInventory().equals(shopInventory.getInventory())) {
            return;
        }

        ItemNode itemNode = shopInventory.getItemNode(event.getSlot());

        if (itemNode == null) {
            return;
        }

        if (itemNode.getPrice() == 0) {
            shopInventory.setChildItemsFromItemNode(itemNode);
            event.setCancelled(true);
            return;
        }

        if (!player.getInventory().contains(itemNode.getPriceMaterial(), itemNode.getPrice())) {
            player.sendMessage("Not enough items.");
            player.closeInventory();
            event.setCancelled(true);
            return;
        }

        ItemStack itemsToRemove = new ItemBuilder(itemNode.getPriceMaterial()).setAmount(itemNode.getPrice()).toItemStack();
        player.getInventory().removeItem(itemsToRemove);
        ItemStack itemStack = itemNode.getItem();
        player.getInventory().addItem(itemStack);
        event.setCancelled(true);
    }
}
