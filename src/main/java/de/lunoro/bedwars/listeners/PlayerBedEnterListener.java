package de.lunoro.bedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class PlayerBedEnterListener implements Listener {

    @EventHandler
    public void onBed(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }
}
