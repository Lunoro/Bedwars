package de.lunoro.bedwars.game;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import de.lunoro.bedwars.game.timer.GameTimer;
import de.lunoro.bedwars.game.team.TeamContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public class Game {

    private final TeamContainer teamContainer;
    private final ItemSpawnerContainer itemContainer;
    private final Plugin plugin;
    private final GameTimer gameTimer;
    private final int playerCountToStart;
    private int taskId;
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final Location spawnLocation;
    @Getter
    private final Location endLocation;
    @Getter
    private final Location spectatorLocation;

    public Game(Plugin plugin, ConfigContainer configContainer, TeamContainer teamContainer, ItemSpawnerContainer itemContainer) {
        this.plugin = plugin;
        this.teamContainer = teamContainer;
        this.itemContainer = itemContainer;

        gamePhase = GamePhase.START;
        gameTimer = new GameTimer();

        playerCountToStart = configContainer.getFile("config").getFileConfiguration().getInt("playerCountToStart");
        spawnLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spawn");
        endLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("end");
        spectatorLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spectator");
    }

    public void start() {
        if (Bukkit.getOnlinePlayers().size() >= playerCountToStart) {
            startGame(false);
        }
    }

    public void forceStart() {
        startGame(true);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        gamePhase = GamePhase.END;
        teamContainer.teleportEachTeam(endLocation);
    }

    public void save() {
        itemContainer.save();
        teamContainer.save();
    }

    private void startGame(boolean isForceStart) {
        startGameClock(isForceStart);
        gamePhase = GamePhase.RUNNING;
        teamContainer.spawnEachTeam();
    }

    private void startGameClock(boolean isForceStart) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (gameTimer.getTimer() < 0) {
                printStartTimer(isForceStart, gameTimer.getTimer() * -1);
            }
            gameTimer.update(gameTimer.getTimer());
            itemContainer.updateItemSpawner(gameTimer.getTimer());
        }, 0, 20);
    }

    private void printStartTimer(boolean isForceStart, int intToPrint) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(ChatColor.YELLOW + "" + intToPrint, "", 10, 20, 10);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1f, 1f);
        });
        if (!isForceStart && Bukkit.getOnlinePlayers().size() >= playerCountToStart) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }
}