package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@AllArgsConstructor
public class BlockBreakListener implements Listener {

    private final TeamContainer teamContainer;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.getType().name().contains("BED")) {
            return;
        }

        Team team = teamContainer.getTeamOfPlayer(event.getPlayer());
        if (team != null) {
            if ((getAdjoiningBedLocationOnZAndXAxis(team.getBedLocation()) != null
                    && getAdjoiningBedLocationOnZAndXAxis(team.getBedLocation()).equals(event.getBlock().getLocation()))
                    || team.getBedLocation().equals(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
        }
        event.setDropItems(false);
    }

    private Location getAdjoiningBedLocationOnZAndXAxis(Location location) {
        for (int i = location.getBlockZ() - 1; i <= location.getBlockZ() + 1; i++) {
            Location bedLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), i);
            if (bedLocation.getBlock().getType().name().contains("BED")) {
                if (!location.equals(bedLocation)) {
                    return bedLocation;
                }
            }
        }
        return getAdjoiningBedLocationOnXAxis(location);
    }

    private Location getAdjoiningBedLocationOnXAxis(Location location) {
        for (int i = location.getBlockX() - 1; i <= location.getBlockX() + 1; i++) {
            Location bedLocation = new Location(location.getWorld(), i, location.getBlockY(), location.getBlockZ());
            if (location.getBlock().getType().name().contains("BED")) {
                if (!location.equals(bedLocation)) {
                    return bedLocation;
                }
            }
        }
        return null;
    }
}