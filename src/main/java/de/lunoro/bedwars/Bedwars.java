package de.lunoro.bedwars;

import de.lunoro.bedwars.commands.CreateTeamCommand;
import de.lunoro.bedwars.commands.ForceStartCommand;
import de.lunoro.bedwars.commands.SetLocationCommand;
import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.Game;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import de.lunoro.bedwars.game.team.TeamContainer;
import de.lunoro.bedwars.listeners.PlayerDeathListener;
import de.lunoro.bedwars.listeners.PlayerJoinListener;
import de.lunoro.bedwars.listeners.PlayerQuitListener;
import de.lunoro.bedwars.listeners.PlayerRespawnListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private Game game;
    private ConfigContainer configContainer;
    private TeamContainer teamContainer;
    private ItemSpawnerContainer itemSpawnerContainer;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        init();
        registerEvents();
        registerCommands();
    }

    private void init() {
        configContainer = new ConfigContainer(this);
        teamContainer = new TeamContainer(configContainer);
        itemSpawnerContainer = new ItemSpawnerContainer(configContainer);
        if (!(configContainer.getFile("config").getFileConfiguration().getBoolean("startInBuildingMode"))) {
            game = new Game(this, configContainer, teamContainer, itemSpawnerContainer);
        }
    }

    public void onDisable() {
        game.save();
        configContainer.getFile("locations").save();
        saveDefaultConfig();
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("createteam").setExecutor(new CreateTeamCommand(teamContainer));
        Bukkit.getPluginCommand("setlocation").setExecutor(new SetLocationCommand(configContainer, teamContainer));
        Bukkit.getPluginCommand("forcestart").setExecutor(new ForceStartCommand(game));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(game, teamContainer), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(game), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }
}

