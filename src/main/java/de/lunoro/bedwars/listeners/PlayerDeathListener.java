package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.team.Team;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private final Game game;

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();
        Team team = game.getTeamContainer().getTeamOfPlayer(player);

        if (player.equals(killer) || killer != null) {
            event.setDeathMessage(ChatColor.RED + killer.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + player.getDisplayName());
            return;
        }

        team.getTeamMember(player).switchRespawn();
        event.setDeathMessage(ChatColor.RED + "☠ " + ChatColor.WHITE + player.getDisplayName());
        game.stopIfGameIsOver();
    }
}
