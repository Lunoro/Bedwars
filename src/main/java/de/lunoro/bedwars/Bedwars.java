package de.lunoro.bedwars;

import de.lunoro.bedwars.commands.*;
import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.listeners.*;
import de.lunoro.bedwars.world.WorldLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private Game game;
    private ConfigContainer configContainer;
    private WorldLoader worldLoader;
    private boolean isStartedInBuildingMode;

    @Override
    public void onLoad() {
        saveResource("config.yml", false);
        saveResource("shopinventory.yml", false);
        init();
        worldLoader.replaceWorld();
    }

    @Override
    public void onEnable() {
        registerEvents();
        registerCommands();
    }

    private void init() {
        configContainer = new ConfigContainer(this);
        final Config defaultConfig = configContainer.getFile("config");
        worldLoader = new WorldLoader(defaultConfig.getString("worldName"), this);
        isStartedInBuildingMode = defaultConfig.getFileConfiguration().getBoolean("startInBuildingMode");
        game = new Game(this, configContainer);
    }

    public void onDisable() {
        game.shutdown();
        saveLocations();
        saveResource("config.yml", false);
        saveResource("shopinventory.yml", false);
    }

    private void saveLocations() {
        if (game.isLocationsLoadedSuccessfully()) {
            configContainer.getFile("locations").save();
        }
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("createteam").setExecutor(new CreateTeamCommand(game.getTeamContainer()));
        Bukkit.getPluginCommand("setlocation").setExecutor(new SetLocationCommand(configContainer, game.getTeamContainer()));
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStartCommand(game, isStartedInBuildingMode));
        Bukkit.getPluginCommand("createitemspawner").setExecutor(new CreateItemSpawnerCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("additemspawnerlocation").setExecutor(new AddItemSpawnerLocationCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("removeitemspawnerlocation").setExecutor(new RemoveItemSpawnerLocationCommand(game.getItemContainer()));
        Bukkit.getPluginCommand("removeteam").setExecutor(new RemoveTeamCommand(game.getTeamContainer()));
        Bukkit.getPluginCommand("jointeam").setExecutor(new JoinTeamCommand(game));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(game, isStartedInBuildingMode), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerBedEnterListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(game, isStartedInBuildingMode), this);
        Bukkit.getPluginManager().registerEvents(new BedBreakListener(game.getTeamContainer()), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(game), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(game), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(game), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnEntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(game.getShopInventoryRegistry()), this);
    }
}
