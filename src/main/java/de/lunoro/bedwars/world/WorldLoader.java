package de.lunoro.bedwars.world;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WorldLoader {

    private final String worldName;
    private final Plugin plugin;

    public WorldLoader(String worldName, Plugin plugin) {
        this.worldName = worldName;
        this.plugin = plugin;
    }

    public void replaceWorld() {
        try {
            FileUtils.deleteDirectory(FileUtils.getFile(worldName));
            final File mapDirectory = FileUtils.getFile(plugin.getDataFolder() + "//" + worldName);
            if (!mapDirectory.exists()) {
                plugin.getLogger().log(Level.WARNING, "No map found. Please add a world to the plugin directory.");
                return;
            }
            FileUtils.copyDirectory(mapDirectory, FileUtils.getFile(worldName));
            plugin.getLogger().log(Level.INFO, "Map found! The default world was reset.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}