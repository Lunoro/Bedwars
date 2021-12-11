package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import de.lunoro.bedwars.game.team.Team;
import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@AllArgsConstructor
public class PlayerRespawnListener implements Listener {

    private final Game game;

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = game.getTeamContainer().getTeamOfPlayer(player);

        if (game.getGamePhase().equals(GamePhase.START)) {
            return;
        }

        if (!team.hasBed()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpectatorLocation());
            return;
        }

        player.setBedSpawnLocation(team.getSpawnLocation(), true);
        player.teleport(team.getSpawnLocation());
    }
}
