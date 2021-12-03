package de.lunoro.bedwars.game;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.spawner.ItemSpawner;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.timer.GameTimer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    private final List<Team> teamList;
    private final List<ItemSpawner> itemSpawnerList;
    private final Plugin plugin;
    private GamePhase gamePhase;
    private final GameTimer gameTimer;
    private final int playerCountToStart;
    private int taskId;
    @Getter
    private final Location endLocation;
    @Getter
    private final Location spectatorLocation;

    public Game(Plugin plugin, ConfigContainer configContainer) {
        this.plugin = plugin;
        teamList = new ArrayList<>();
        itemSpawnerList = new ArrayList<>();
        gamePhase = GamePhase.START;
        gameTimer = new GameTimer();
        System.out.println(configContainer.getFile("config").getFileConfiguration().getInt("playerCountToStart"));
        playerCountToStart = 2;
        endLocation = (Location) configContainer.getFile("locations").getFileConfiguration().get("endLocation");
        spectatorLocation = (Location) configContainer.getFile("locations").getFileConfiguration().get("spectatorLocation");
    }

    public void start() {
        if (Bukkit.getOnlinePlayers().size() >= playerCountToStart) {
            startStartTimer(false);
        }
    }

    public void forceStart() {
        startStartTimer(true);
    }

    public void stop() {
        if (gamePhase != GamePhase.RUNNING) {
            return;
        }
        Bukkit.getScheduler().cancelTask(taskId);
        gamePhase = GamePhase.END;
        teleportEachTeamToEnd();
    }

    private void startStartTimer(boolean isForceStart) {
        AtomicInteger atomicInteger = new AtomicInteger(11);
        Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.YELLOW + "" + atomicInteger.decrementAndGet(), ""));
            if (!isForceStart && Bukkit.getOnlinePlayers().size() >= playerCountToStart) {
                bukkitTask.cancel();
            }
            if (atomicInteger.get() == 1) {
                startGame();
                bukkitTask.cancel();
            }
        }, 0, 20);
    }

    private void startGame() {
        gamePhase = GamePhase.RUNNING;
        spawnEachTeam();
        startGameClock();
    }

    private void startGameClock() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            gameTimer.update(gameTimer.getTimer());
            updateItemSpawner(gameTimer.getTimer());
        }, 0, 20);
    }

    private void updateItemSpawner(int gameTicks) {
        for (ItemSpawner itemSpawner : itemSpawnerList) {
            itemSpawner.update(gameTicks);
        }
    }

    private void spawnEachTeam() {
        for (Team team : teamList) {
            team.spawnTeam();
        }
    }

    private void teleportEachTeamToEnd() {
        for (Team team : teamList) {
            team.teleportTeam(endLocation);
        }
    }

    public Team getTeamOfPlayer(Player player) {
        for (Team team : teamList) {
            if (team.getTeamMember(player) != null) {
                return team;
            }
        }
        return null;
    }

    public void addSpawner(ItemSpawner itemSpawner) {
        itemSpawnerList.add(itemSpawner);
    }

    public void removeSpawner(ItemSpawner itemSpawner) {
        itemSpawnerList.remove(itemSpawner);
    }
}