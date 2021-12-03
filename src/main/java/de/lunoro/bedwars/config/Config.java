package de.lunoro.bedwars.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Config {

    private final java.io.File file;
    private FileConfiguration config;

    public Config(java.io.File file, FileConfiguration config) {
        this.file = file;
        this.config = config;
    }

    @Deprecated
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Cant save the file: " + file.getAbsolutePath(), e);
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void clear() {
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }
    }

    public FileConfiguration getFileConfiguration() {
        return config;
    }
}