package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.builder.ItemBuilder;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopInventory {

    @Getter
    private final Inventory inventory;
    private final List<ItemNode> itemNodeList;

    public ShopInventory(List<ItemNode> itemNodeList, int inventorySize) {
        this.inventory = Bukkit.getServer().createInventory(null, inventorySize, "Shop");
        this.itemNodeList = itemNodeList;
        setRootItemsIntoInventory();
    }

    public void setRootItemsIntoInventory() {
        for (ItemNode rootNode : itemNodeList) {
            inventory.setItem(rootNode.getIndex(), rootNode.getItem());
        }
    }

    public void setChildItemsFromItemNode(ItemNode itemNode) {
        removeAllChildItems();
        if (itemNode.getChildrenList().isEmpty()) {
            return;
        }
        for (ItemNode children : itemNode.getChildrenList()) {
            inventory.setItem(children.getIndex(), children.getItem());
        }
    }

    private void removeAllChildItems() {
        ItemStack air = new ItemBuilder(Material.AIR).toItemStack();
        for (ItemNode itemNode : itemNodeList) {
            for (ItemNode child : itemNode.getChildrenList()) {
                inventory.setItem(child.getIndex(), air);
            }
        }
    }

    public ItemNode getItemNode(int inventoryIndex) {
        for (ItemNode itemNode : itemNodeList) {
            if (itemNode.getIndex() == inventoryIndex) {
                return itemNode;
            }
            for (ItemNode child : itemNode.getChildrenList()) {
                if (child.getIndex() == inventoryIndex) {
                    return child;
                }
            }
        }
        return null;
    }
}
