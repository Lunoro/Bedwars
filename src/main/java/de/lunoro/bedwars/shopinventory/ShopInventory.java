package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ShopInventory {

    private final Inventory inventory;
    private final ShopInventoryLoader shopInventoryLoader;
    private final List<ItemNode> itemNodeList;

    public ShopInventory(ConfigContainer configContainer) {
        Config shopInventoryConfig = configContainer.getFile("shopinventory");
        this.inventory = Bukkit.getServer().createInventory(null, shopInventoryConfig.getFileConfiguration().getInt("inventorySize"));
        this.shopInventoryLoader = new ShopInventoryLoader(shopInventoryConfig);
        this.itemNodeList = shopInventoryLoader.loadItemNodes();
        setRootItemsIntoInventory();
    }

    public void setRootItemsIntoInventory() {
        for (ItemNode rootNode : itemNodeList) {
            inventory.setItem(rootNode.getIndex(), rootNode.getItem());
        }
    }

    public void setChildItemsFromItemNode(ItemNode itemNode) {
        for (ItemNode children : itemNode.getChildrenList()) {
            inventory.setItem(children.getIndex(), children.getItem());
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public ItemNode getItemNode(int inventoryIndex) {
        for (ItemNode itemNode : itemNodeList) {
            if (itemNode.getIndex() == inventoryIndex) {
                return itemNode;
            }
        }
        return null;
    }
}
