package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@AllArgsConstructor
public class PlayerRespawnListener implements Listener {

    private final Game game;
    private final TeamContainer teamContainer;

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Team team = teamContainer.getTeamOfPlayer(player);

        if (team == null || !team.hasBed()) {
            player.teleport(game.getSpectatorLocation());
            return;
        }

        player.teleport(team.getSpawnLocation());
    }
}
