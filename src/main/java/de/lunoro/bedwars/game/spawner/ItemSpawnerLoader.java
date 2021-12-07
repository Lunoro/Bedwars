package de.lunoro.bedwars.game.spawner;

import de.lunoro.bedwars.config.Config;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ItemSpawnerLoader {

    private final Config config;

    public List<ItemSpawner> load() {
        List<ItemSpawner> list = new ArrayList<>();
        FileConfiguration spawnerFileConfig = config.getFileConfiguration();
        for (String key : spawnerFileConfig.getKeys(false)) {
            String materialName = key;
            List<Location> locationList = (List<Location>) spawnerFileConfig.getConfigurationSection(key).getList("locationList");
            double dropDuration = spawnerFileConfig.getConfigurationSection(key).getDouble("dropduration");

            ItemSpawner itemSpawner = new ItemSpawner(materialName, locationList, dropDuration);
            list.add(itemSpawner);
        }
        return list;
    }

    public void save(List<ItemSpawner> itemSpawnerList) {
        config.clear();
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            saveInConfigurationSection(itemSpawner, config.getFileConfiguration().createSection(itemSpawner.getMaterial().name()));
        }
        config.save();
    }

    private void saveInConfigurationSection(ItemSpawner itemSpawner, ConfigurationSection configurationSection) {
        configurationSection.set("locationList", itemSpawner.getLocationList());
        configurationSection.set("dropduration", itemSpawner.getDropDuration());
    }
}
