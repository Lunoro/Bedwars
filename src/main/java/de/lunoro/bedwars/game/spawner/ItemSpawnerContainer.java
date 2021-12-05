package de.lunoro.bedwars.game.spawner;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ItemSpawnerContainer {

    private final List<ItemSpawner> itemSpawnerList;
    private final ConfigContainer configContainer;

    public ItemSpawnerContainer(ConfigContainer configContainer) {
        this.itemSpawnerList = loadTeams();
        this.configContainer = configContainer;
    }

    private List<ItemSpawner> loadTeams() {
        List<ItemSpawner> list = new ArrayList<>();
        FileConfiguration spawnerFileConfig = configContainer.getFile("spawner").getFileConfiguration();
        for (String key : spawnerFileConfig.getKeys(false)) {

            String materialName = spawnerFileConfig.getConfigurationSection(key).getString("name");
            List<Location> locationList = (List<Location>) spawnerFileConfig.getConfigurationSection(key).getList("locationList");
            int dropDuration = spawnerFileConfig.getConfigurationSection(key).getInt("dropduration");

            ItemSpawner itemSpawner = new ItemSpawner(materialName, locationList, dropDuration);
            list.add(itemSpawner);
        }
        return list;
    }

    public void save() {
        int i = 0;
        Config spawnerConfig = configContainer.getFile("spawner");
        spawnerConfig.clear();
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            saveInConfigurationSection(itemSpawner, spawnerConfig.getFileConfiguration().createSection(String.valueOf(i)));
            i++;
        }
        spawnerConfig.save();
    }

    private void saveInConfigurationSection(ItemSpawner itemSpawner, ConfigurationSection configurationSection) {
        configurationSection.set("name", itemSpawner.getMaterial().name());
        configurationSection.set("locationList", itemSpawner.getLocationList());
        configurationSection.set("dropduration", itemSpawner.getDropDuration());
    }


    public void updateItemSpawner(int gameTicks) {
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            itemSpawner.update(gameTicks);
        }
    }

}
