package de.lunoro.bedwars.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashMap;

public class ConfigContainer {

    private final Plugin plugin;
    private final HashMap<String, Config> files = new HashMap<>();

    public ConfigContainer(Plugin plugin) {
        this.plugin = plugin;
    }

    public Config getFile(String name) {
        if (files.containsKey(name)) {
            return files.get(name);
        }
        final Config config = create(plugin, name + ".yml");
        files.put(name, config);
        return config;
    }

    public Config create(Plugin plugin, String configName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        java.io.File configFile = new java.io.File(plugin.getDataFolder(), configName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Config(configFile, YamlConfiguration.loadConfiguration(configFile));
    }
}
