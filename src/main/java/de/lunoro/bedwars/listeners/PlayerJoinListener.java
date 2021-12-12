package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
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
            player.sendMessage("The server is in building mode so no game will be started.");
            return;
        }

        if (player.isDead()) {
            player.spigot().respawn();
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(game.getSpawnLocation());

        if (game.getGamePhase().equals(GamePhase.RUNNING)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpectatorLocation());
            return;
        }

        game.start();
    }
}
