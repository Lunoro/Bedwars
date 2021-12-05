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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Team team = game.getTeamContainer().getTeamOfPlayer(player);

        player.setGameMode(GameMode.SURVIVAL);
        if (game.getGamePhase().equals(GamePhase.RUNNING) && team == null) {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(game.getSpectatorLocation());
            return;
        }

        game.start();
    }
}
