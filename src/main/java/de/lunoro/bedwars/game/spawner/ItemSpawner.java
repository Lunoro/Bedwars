package de.lunoro.bedwars.game.spawner;

import de.lunoro.bedwars.game.IGameObject;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemSpawner implements IGameObject {

    private final Material material;
    private final List<Location> locationList;
    private final double dropDuration;

    public ItemSpawner(Material material, double dropDuration) {
        this.material = material;
        this.dropDuration = dropDuration;
        locationList = new ArrayList<>();
    }

    public ItemSpawner(String materialName, List<Location> locationList, double dropDuration) {
        this.material = Material.getMaterial(materialName);
        this.dropDuration = dropDuration;
        this.locationList = locationList;
    }

    @Override
    public void update(int gameTick) {
        if (gameTick % dropDuration == 0) {
            for (Location location : locationList) {
                location.getWorld().dropItem(location, new ItemStack(material));
            }
        }
    }

    @Override
    public void render(int gameTick) {

    }

    public void addLocation(Location location) {
        locationList.add(location);
    }

    public void removeLocation(Location location) {
        locationList.remove(location);
    }
}
