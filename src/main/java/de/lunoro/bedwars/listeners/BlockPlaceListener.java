package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@AllArgsConstructor
public class BlockPlaceListener implements Listener {

    private final Game game;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (game.getGamePhase().equals(GamePhase.RUNNING)) {
            game.getPlacedBlocksContainer().addBlock(event.getBlock());
        }
    }
}
