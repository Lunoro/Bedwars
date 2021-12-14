package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import lombok.Getter;

import java.util.List;

public class ItemNodeContainer {

    @Getter
    private final List<ItemNode> itemNodeList;

    public ItemNodeContainer(ConfigContainer configContainer) {
        Config shopInventoryConfig = configContainer.getFile("shopinventory");
        ItemNodeLoader itemNodeLoader = new ItemNodeLoader(shopInventoryConfig);
        itemNodeList = itemNodeLoader.load();
    }
}
