package de.lunoro.bedwars.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();

        if (player.equals(killer) || killer != null) {
            event.setDeathMessage(ChatColor.RED + killer.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + player.getDisplayName());
            return;
        }

        event.setDeathMessage(ChatColor.RED + "☠ " + ChatColor.WHITE + player.getDisplayName());
        player.spigot().respawn();
    }
}
