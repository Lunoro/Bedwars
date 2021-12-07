package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopInventory {

    private Inventory inventory;
    private final ConfigContainer configContainer;

    public ShopInventory(ConfigContainer configContainer) {
        this.configContainer = configContainer;
    }

    private void load() {
        FileConfiguration config = configContainer.getFile("config").getFileConfiguration();
        inventory = Bukkit.createInventory(null, config.getInt("shopinvetorysize"));
    }

    public void setRootItem(int index, ItemNode item) {
        inventory.setItem(index, item.getRootItemStack());
    }

    public void setChildrenIntoInventory(int startOfChildrenLine, ItemNode item) {
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
