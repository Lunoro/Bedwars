package de.lunoro.bedwars.shopinventory.item;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ItemNodeLoader {

    private final Config shopInventoryConfig;

    public List<ItemNode> loadItemNodes() {
        List<ItemNode> itemNodeList = new ArrayList<>();
        ConfigurationSection inventoryFileConfig = shopInventoryConfig.getFileConfiguration().getConfigurationSection("items");
        for (String key : inventoryFileConfig.getKeys(false)) {
            String materialName = inventoryFileConfig.getConfigurationSection(key).getString("materialName");
            String name = inventoryFileConfig.getConfigurationSection(key).getString("name");
            String enchantment = inventoryFileConfig.getConfigurationSection(key).getString("enchantment");
            int enchantmentLevel = inventoryFileConfig.getConfigurationSection(key).getInt("enchantmentLevel");
            String description = inventoryFileConfig.getConfigurationSection(key).getString("description");
            String priceItemName = inventoryFileConfig.getConfigurationSection(key).getString("priceItem");
            int price = inventoryFileConfig.getConfigurationSection(key).getInt("price");
            int inventoryIndex = inventoryFileConfig.getConfigurationSection(key).getInt("inventoryIndex");
            List<ItemNode> childrenList = loadChildren(inventoryFileConfig.getConfigurationSection(key));
            Material parentMaterial = Material.getMaterial(materialName);
            Material priceItem = Material.getMaterial(priceItemName);
            if (parentMaterial == null || priceItem == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Inventory Config konnte nicht geladen werden.");
                return null;
            }
            ItemNode itemNode = new ItemNode(parentMaterial, name, priceItem, price, inventoryIndex, childrenList);
            itemNode.addEnchantment(enchantment, enchantmentLevel);
            itemNode.addLore(description);
            itemNodeList.add(itemNode);
        }
        return itemNodeList;
    }

    private List<ItemNode> loadChildren(ConfigurationSection inventoryFileConfig) {
        List<ItemNode> childrenList = new ArrayList<>();
        ConfigurationSection childrenSection = inventoryFileConfig.getConfigurationSection("children");
        for (String childrenSectionKey : childrenSection.getKeys(false)) {
            String childrenMaterialName = childrenSection.getConfigurationSection(childrenSectionKey).getString("materialName");
            String childrenName = childrenSection.getConfigurationSection(childrenSectionKey).getString("name");
            String childrenEnchantment = childrenSection.getConfigurationSection(childrenSectionKey).getString("enchantment");
            int childrenEnchantmentLevel = childrenSection.getConfigurationSection(childrenSectionKey).getInt("enchantmentLevel");
            String childrenDescription = childrenSection.getConfigurationSection(childrenSectionKey).getString("description");
            int childrenIndex = childrenSection.getConfigurationSection(childrenSectionKey).getInt("inventoryIndex");
            String childrenPriceItemName = childrenSection.getConfigurationSection(childrenSectionKey).getString("priceItem");
            int childrenPrice = childrenSection.getConfigurationSection(childrenSectionKey).getInt("price");
            Material childrenMaterial = Material.getMaterial(childrenMaterialName);
            Material childrenPriceItem = Material.getMaterial(childrenPriceItemName);
            if (childrenMaterial == null || childrenPriceItem == null) {
                continue;
            }
            ItemNode itemNode = new ItemNode(childrenMaterial, childrenName, childrenPriceItem, childrenPrice, childrenIndex, new ArrayList<>());
            itemNode.addEnchantment(childrenEnchantment, childrenEnchantmentLevel);
            itemNode.addLore(childrenDescription);
            childrenList.add(itemNode);
        }
        return childrenList;
    }
}
