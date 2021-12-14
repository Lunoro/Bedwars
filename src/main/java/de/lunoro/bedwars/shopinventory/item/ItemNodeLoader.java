package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.config.Config;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ItemNodeLoader {

    private final Config shopInventory;

    public List<ItemNode> load() {
        List<ItemNode> itemNodeList = new ArrayList<>();
        ConfigurationSection parentSection = shopInventory.getFileConfiguration().getConfigurationSection("items");

        for (String key : parentSection.getKeys(false)) {
            ItemNode itemNode = loadItemNode(parentSection.getConfigurationSection(key));
            itemNodeList.add(itemNode);
        }
        return itemNodeList;
    }

    private ItemNode loadItemNode(ConfigurationSection section) {
        String materialName = section.getString("materialName");
        String name = section.getString("name");
        String enchantment = section.getString("enchantment");
        int enchantmentLevel = section.getInt("enchantmentLevel");
        String description = section.getString("description");
        String priceItemName = section.getString("priceItem");
        int price = section.getInt("price");
        int amount = section.getInt("amount");
        int inventoryIndex = section.getInt("inventoryIndex");
        List<ItemNode> childrenList = loadChildren(section.getConfigurationSection("children"));

        Material material = Material.getMaterial(materialName);
        Material priceItem = Material.getMaterial(priceItemName);

        if (material == null) {
            System.out.println("Material not found!");
            return null;
        }
        if (priceItem == null) {
            priceItem = Material.DIAMOND;
        }

        ItemNode itemNode = new ItemNode(material, name, priceItem, price, amount, inventoryIndex, childrenList);
        itemNode.addEnchantment(enchantment, enchantmentLevel);
        itemNode.addLore(description);
        return itemNode;
    }

    private List<ItemNode> loadChildren(ConfigurationSection childSection) {
        List<ItemNode> childrenList = new ArrayList<>();
        if (childSection == null) {
            return childrenList;
        }

        for (String key : childSection.getKeys(false)) {
            childrenList.add(loadItemNode(childSection.getConfigurationSection(key)));
        }

        return childrenList;
    }
}
