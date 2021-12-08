package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopInventory {

    private final Inventory inventory;
    private final ConfigContainer configContainer;
    private final ShopInventoryLoader shopInventoryLoader;
    private final List<ItemNode> itemNodeList;

    public ShopInventory(ConfigContainer configContainer) {
        this.configContainer = configContainer;
        Config shopInventoryConfig = configContainer.getFile("shopinventory");
        this.shopInventoryLoader = new ShopInventoryLoader(shopInventoryConfig);
    }

    public void setRootItem(int index, ItemNode item) {
        inventory.setItem(index, item.getRootItemStack());
    }

    public void setChildrenIntoInventory(int startOfChildrenLine) {
        List<String> childrenList = shopInventoryLoader.
        int i = startOfChildrenLine;
        for (ItemStack children : item.getChildrenList()) {
            inventory.setItem(i, children);
            i++;
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}
