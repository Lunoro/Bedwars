package de.lunoro.bedwars.blocks;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class PlacedBlocksContainer {

    private final List<Block> placedBlockList;

    public PlacedBlocksContainer() {
        placedBlockList = new ArrayList<>();
    }

    public void addBlock(Block block) {
        placedBlockList.add(block);
    }

    public void removeBlock(Block block) {
        placedBlockList.remove(block);
    }

    public boolean blockWasPlacedByPlayer(Block block) {
        return placedBlockList.contains(block);
    }
}
