package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.shopinventory.item.ItemNodeContainer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ShopInventoryRegistry {

    private final ItemNodeContainer itemNodeContainer;
    private final Map<Player, ShopInventory> shopInventoryMap;
    private final int shopInventorySize;

    public ShopInventoryRegistry(ConfigContainer configContainer) {
        this.shopInventoryMap = new HashMap<>();
        this.itemNodeContainer = new ItemNodeContainer(configContainer);
        this.shopInventorySize = configContainer.getFile("shopinventory").getFileConfiguration().getInt("inventorySize");
    }

    public ShopInventory getInventory(Player player) {
        if (shopInventoryMap.get(player) == null) {
            addShopInventoryToPlayerIfNotExists(player);
            return shopInventoryMap.get(player);
        }
        return shopInventoryMap.get(player);
    }

    public void deleteInventory(Player player) {
        shopInventoryMap.remove(player);
    }

    private void addShopInventoryToPlayerIfNotExists(Player player) {
        ShopInventory shopInventory = new ShopInventory(itemNodeContainer.getItemNodeList(), shopInventorySize);
        shopInventoryMap.put(player, shopInventory);
    }
}
