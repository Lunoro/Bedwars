package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.blocks.PlacedBlocksContainer;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.GamePhase;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@AllArgsConstructor
public class BlockBreakListener implements Listener {

    private final Game game;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!game.getGamePhase().equals(GamePhase.RUNNING)) {
            return;
        }

        PlacedBlocksContainer placedBlocksContainer = game.getPlacedBlocksContainer();

        if (placedBlocksContainer.blockWasPlacedByPlayer(event.getBlock())) {
            placedBlocksContainer.removeBlock(event.getBlock());
        }
    }
}
