package de.lunoro.bedwars.game.spawner;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import org.bukkit.Location;

import java.util.List;

public class ItemSpawnerContainer {

    private final List<ItemSpawner> itemSpawnerList;
    private final ConfigContainer configContainer;
    private final ItemSpawnerLoader itemSpawnerLoader;

    public ItemSpawnerContainer(ConfigContainer configContainer) {
        this.configContainer = configContainer;

        Config spawnerConfig = configContainer.getFile("spawner");
        this.itemSpawnerLoader = new ItemSpawnerLoader(spawnerConfig);
        this.itemSpawnerList = itemSpawnerLoader.load();
    }

    public void save() {
        itemSpawnerLoader.save(itemSpawnerList);
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
