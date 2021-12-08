package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import lombok.AllArgsConstructor;
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

        //if (isBuildingMode || !game.getGamePhase().equals(GamePhase.RUNNING)) {
        //    return;
        //}

        Inventory inventory = game.getShopInventory().getInventory();
        player.openInventory(inventory);
        event.setCancelled(true);
    }
}
