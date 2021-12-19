package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import de.lunoro.bedwars.shopinventory.ShopInventory;
import lombok.AllArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

@AllArgsConstructor
public class PlayerInteractEntityListener implements Listener {

    private final Game game;
    private final boolean isBuildingMode;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (isBuildingMode || !game.getGamePhase().equals(GamePhase.RUNNING)) {
            return;
        }

        if (event.getRightClicked().getType().equals(EntityType.PLAYER)) {
            return;
        }

        ShopInventory shopInventory = game.getShopInventoryRegistry().getInventory(player);
        Inventory inventory = shopInventory.getInventory();
        player.openInventory(inventory);
        event.setCancelled(true);
    }
}
