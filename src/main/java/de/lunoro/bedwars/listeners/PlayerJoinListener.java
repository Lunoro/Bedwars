package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final Game game;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        game.start();
    }
}
