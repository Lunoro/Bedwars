package de.lunoro.bedwars.game.spawner;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ItemSpawnerContainer {

    private final List<ItemSpawner> itemSpawnerList;
    private final ConfigContainer configContainer;

    public ItemSpawnerContainer(ConfigContainer configContainer) {
        this.configContainer = configContainer;
        this.itemSpawnerList = loadTeams();
    }

    private List<ItemSpawner> loadTeams() {
        List<ItemSpawner> list = new ArrayList<>();
        FileConfiguration spawnerFileConfig = configContainer.getFile("spawner").getFileConfiguration();
        for (String key : spawnerFileConfig.getKeys(false)) {

            String materialName = key;
            List<Location> locationList = (List<Location>) spawnerFileConfig.getConfigurationSection(key).getList("locationList");
            double dropDuration = spawnerFileConfig.getConfigurationSection(key).getDouble("dropduration");

            ItemSpawner itemSpawner = new ItemSpawner(materialName, locationList, dropDuration);
            list.add(itemSpawner);
        }
        return list;
    }

    public void save() {
        Config spawnerConfig = configContainer.getFile("spawner");
        spawnerConfig.clear();
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            saveInConfigurationSection(itemSpawner, spawnerConfig.getFileConfiguration().createSection(itemSpawner.getMaterial().name()));
        }
        spawnerConfig.save();
    }

    private void saveInConfigurationSection(ItemSpawner itemSpawner, ConfigurationSection configurationSection) {
        configurationSection.set("locationList", itemSpawner.getLocationList());
        configurationSection.set("dropduration", itemSpawner.getDropDuration());
    }

    public void add(ItemSpawner itemSpawner) {
        itemSpawnerList.add(itemSpawner);
    }

    public void removeNearestItemSpawnerLocation(ItemSpawner itemSpawner, Location targetLocation) {
        double cache = itemSpawner.getLocationList().get(0).distance(targetLocation);
        Location endLocation = itemSpawner.getLocationList().get(0);
        for (Location location : itemSpawner.getLocationList()) {
            if (cache > targetLocation.distance(location)) {
                cache = targetLocation.distance(location);
                endLocation = location;
            }
        }
        itemSpawner.removeLocation(endLocation);
    }

    public void remove(ItemSpawner itemSpawner) {
        itemSpawnerList.remove(itemSpawner);
    }

    public ItemSpawner getItemSpawner(String materialName) {
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            if (itemSpawner.getMaterial().name().equals(materialName)) {
                return itemSpawner;
            }
        }
        return null;
    }

    public void updateItemSpawner(int gameTicks) {
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            itemSpawner.update(gameTicks);
        }
    }

}
