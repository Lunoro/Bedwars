package de.lunoro.bedwars.game;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.timer.GameTimer;
import de.lunoro.bedwars.game.team.TeamContainer;
import de.lunoro.bedwars.shopinventory.ShopInventoryRegistry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collections;

public class Game {

    @Getter
    private final TeamContainer teamContainer;
    @Getter
    private final ItemSpawnerContainer itemContainer;
    @Getter
    private final ShopInventoryRegistry shopInventoryRegistry;
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final Location spawnLocation;
    @Getter
    private final Location endLocation;
    @Getter
    private final Location spectatorLocation;
    private final boolean stopServerIfGameIsOver;
    private final Plugin plugin;
    private final GameTimer gameTimer;
    private final int maxPlayersAmountInATeam;
    private final int playerCountToStart;
    private int taskId;

    public Game(Plugin plugin, ConfigContainer configContainer) {
        this.plugin = plugin;
        this.teamContainer = new TeamContainer(configContainer);
        this.itemContainer = new ItemSpawnerContainer(configContainer);
        this.shopInventoryRegistry = new ShopInventoryRegistry(configContainer);

        this.gamePhase = GamePhase.START;
        this.gameTimer = new GameTimer();

        this.playerCountToStart = configContainer.getFile("config").getFileConfiguration().getInt("playerCountToStart");
        this.spawnLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spawn");
        this.endLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("end");
        this.spectatorLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spectator");
        this.maxPlayersAmountInATeam = configContainer.getFile("config").getFileConfiguration().getInt("maxPlayersInATeam");
        this.stopServerIfGameIsOver = configContainer.getFile("config").getFileConfiguration().getBoolean("stopServerIfGameIsOver");
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
        respawnAllPlayer();
        gamePhase = GamePhase.END;
        teamContainer.teleportEachTeam(endLocation);
        if (stopServerIfGameIsOver) {
            Bukkit.broadcastMessage("The server will shutdown in 30 seconds.");
            Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getServer().shutdown(), 30 * 20);
        }
    }

    private void respawnAllPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead()) {
                player.spigot().respawn();
            }
        }
    }

    public void shutdown() {
        stop();
        itemContainer.save();
        teamContainer.unregisterAllScoreboardTeams();
        teamContainer.save();
    }

    private void startGame(boolean isForceStart) {
        startGameClock(isForceStart);
    }

    private void startGameClock(boolean isForceStart) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (gameTimer.getTimer() < 0) {
                printStartTimer(isForceStart, gameTimer.getTimer() * -1);
            }
            if (gameTimer.getTimer() == -1) {
                startGameIfTimerIsExpired();
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
        if (!isForceStart && !(Bukkit.getOnlinePlayers().size() >= playerCountToStart)) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void startGameIfTimerIsExpired() {
        System.out.println("Start Game");
        gamePhase = GamePhase.RUNNING;
        assignATeamToEachTeamlessPlayer();
        teamContainer.spawnEachTeam();
    }

    private void assignATeamToEachTeamlessPlayer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addTeamlessPlayerToATeam(player);
        }
    }

    public void addTeamlessPlayerToATeam(Player player) {
        for (Team team : teamContainer.getTeamList()) {
            Collections.shuffle(teamContainer.getTeamList());
            if (team.getTeamSize() == maxPlayersAmountInATeam) {
                continue;
            }
            if (teamContainer.playerHasTeam(player)) {
                break;
            }
            team.addTeamMember(player);
            break;
        }
    }
}