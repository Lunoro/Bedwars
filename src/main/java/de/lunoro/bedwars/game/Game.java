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

    @Getter
    private final TeamContainer teamContainer;
    @Getter
    private final ItemSpawnerContainer itemContainer;
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final Location spawnLocation;
    @Getter
    private final Location endLocation;
    @Getter
    private final Location spectatorLocation;
    private final Plugin plugin;
    private final GameTimer gameTimer;
    private final int playerCountToStart;
    private int taskId;

    public Game(Plugin plugin, ConfigContainer configContainer) {
        this.plugin = plugin;
        this.teamContainer = new TeamContainer(configContainer);
        this.itemContainer = new ItemSpawnerContainer(configContainer);

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

    public void shutdown() {
        stop();
        itemContainer.save();
        teamContainer.save();
        teamContainer.unregisterAllScoreboardTeams();
    }

    private void startGame(boolean isForceStart) {
        startGameClock(isForceStart);
        if (gameTimer.getTimer() == 0) {
            gamePhase = GamePhase.RUNNING;
            teamContainer.spawnEachTeam();
        }
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

    public void stopIfGameIsOver() {
        if (gameHasWinner()) {
            stop();
        }
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        gamePhase = GamePhase.END;
        teamContainer.teleportEachTeam(endLocation);
    }

    private boolean gameHasWinner() {
        return teamContainer.getWinner() != null;
    }
}