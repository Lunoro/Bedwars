package de.lunoro.bedwars;

import de.lunoro.bedwars.commands.*;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.listeners.PlayerDeathListener;
import de.lunoro.bedwars.listeners.PlayerJoinListener;
import de.lunoro.bedwars.listeners.PlayerQuitListener;
import de.lunoro.bedwars.listeners.PlayerRespawnListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private Game game;
    private ConfigContainer configContainer;
    private boolean isStartedInBuildingMode;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        init();
        registerEvents();
        registerCommands();
    }

    private void init() {
        configContainer = new ConfigContainer(this);
        isStartedInBuildingMode = configContainer.getFile("config").getFileConfiguration().getBoolean("startInBuildingMode");
        game = new Game(this, configContainer);
    }

    public void onDisable() {
        game.save();
        configContainer.getFile("locations").save();
        saveDefaultConfig();
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("createteam").setExecutor(new CreateTeamCommand(game.getTeamContainer()));
        Bukkit.getPluginCommand("setlocation").setExecutor(new SetLocationCommand(configContainer, game.getTeamContainer()));
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStartCommand(game, isStartedInBuildingMode));
        Bukkit.getPluginCommand("createitemspawner").setExecutor(new CreateItemSpawnerCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("additemspawnerlocation").setExecutor(new AddItemSpawnerLocationCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("removeitemspawnerlocation").setExecutor(new RemoveItemSpawnerLocationCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("removeteam").setExecutor(new RemoveTeamCommand(game.getTeamContainer()));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(game, isStartedInBuildingMode), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(game.getTeamContainer()), this);
    }
}

