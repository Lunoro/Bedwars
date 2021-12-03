package de.lunoro.bedwars;

import de.lunoro.bedwars.commands.ForceStartCommand;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.listeners.PlayerJoinListener;
import de.lunoro.bedwars.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private Game game;
    private ConfigContainer configContainer;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        init();
        registerEvents();
        registerCommands();
    }

    private void init() {
        configContainer = new ConfigContainer(this);
        game = new Game(this, configContainer);
    }

    public void onDisable() {
        saveDefaultConfig();
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStartCommand(game));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }
}

