package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.Game;
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

        if (!team.hasBed()) {
            team.getTeamMember(player).switchRespawn();
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpectatorLocation());
            return;
        }

        player.teleport(team.getSpawnLocation());
    }
}
