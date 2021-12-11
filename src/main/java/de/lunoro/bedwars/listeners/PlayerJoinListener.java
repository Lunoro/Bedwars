package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import de.lunoro.bedwars.game.team.Team;
import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final Game game;
    private final boolean isStartedInBuildingMode;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (isStartedInBuildingMode) {
            player.sendMessage("Der Server befindet sich im Baumodus es wird also kein Spiel gestartet werden.");
            return;
        }

        if (player.isDead()) {
            player.spigot().respawn();
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(game.getSpawnLocation());

        if (game.getGamePhase().equals(GamePhase.RUNNING) && !game.getTeamContainer().getTeamOfPlayer(player).getTeamMember(player).isRespawnable()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpectatorLocation());
            return;
        }

        game.start();
    }
}
