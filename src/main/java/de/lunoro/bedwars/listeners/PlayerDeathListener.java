package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import de.lunoro.bedwars.game.team.Team;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private final Game game;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = event.getEntity().getKiller();
        Team team = game.getTeamContainer().getTeamOfPlayer(player);

        if (!game.getGamePhase().equals(GamePhase.RUNNING)) {
            return;
        }

        if (killer == null) {
            event.setDeathMessage(ChatColor.RED + "☠ " + ChatColor.WHITE + player.getDisplayName());
        } else {
            event.setDeathMessage(ChatColor.RED + killer.getDisplayName() + ChatColor.GRAY + " -> " + ChatColor.WHITE + player.getDisplayName());
        }

        if (!team.hasBed()) {
            team.getTeamMember(player).switchRespawn();
        }

        game.getTeamContainer().updateStatusOfAllTeams();
        System.out.println(game);

        System.out.println(game.getTeamContainer().getTeamsAlive());
        if (game.getTeamContainer().getTeamsAlive() == 1) {
            Bukkit.broadcastMessage(game.getTeamContainer().getWinnerTeam().getName() + " has won.");
            game.stop();
        }
    }
}
