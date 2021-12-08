package de.lunoro.bedwars.shopinventory;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.shopinventory.item.ItemNode;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

import java.util.List;

@AllArgsConstructor
public class ShopInventoryLoader {

    private final Config shopInventoryConfig;

    public Inventory loadItemNode() {
        FileConfiguration inventoryFileConfig = shopInventoryConfig.getFileConfiguration();
        for (String key : inventoryFileConfig.getKeys(false)) {
            String name = inventoryFileConfig.getConfigurationSection(key).getString("name");
            String materialName = inventoryFileConfig.getConfigurationSection(key).getString("material");
            List<String> childrenList = (List<String>) inventoryFileConfig.getConfigurationSection(key).getList("children");
            Material material = Material.getMaterial(materialName);
            if (material == null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Inventory Config konnte nicht geladen werden.");
                return null;
            }
            ItemNode itemNode = new ItemNode(material, name, childrenList);
        }
        return list;
    }

    public void save(List<Team> teamList) {
        int i = 0;
        config.clear();
        for (Team team : teamList) {
            saveInConfigurationSection(team, config.getFileConfiguration().createSection(String.valueOf(i)));
            i++;
        }
        config.save();
    }

    private void saveInConfigurationSection(Team team, ConfigurationSection configurationSection) {
        configurationSection.set("name", team.getName());
        configurationSection.set("color", team.getColorCode());
        configurationSection.set("spawnlocation", team.getSpawnLocation());
        configurationSection.set("bedlocation", team.getBedLocation());
    }
}
