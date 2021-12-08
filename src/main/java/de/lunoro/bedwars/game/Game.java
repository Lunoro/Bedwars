package de.lunoro.bedwars.game;

import de.lunoro.bedwars.config.ConfigContainer;
import de.lunoro.bedwars.game.spawner.ItemSpawnerContainer;
import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.timer.GameTimer;
import de.lunoro.bedwars.game.team.TeamContainer;
import de.lunoro.bedwars.shopinventory.ShopInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Game {

    @Getter
    private final TeamContainer teamContainer;
    @Getter
    private final ItemSpawnerContainer itemContainer;
    @Getter
    private final ShopInventory shopInventory;
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final Location spawnLocation;
    @Getter
    private final Location endLocation;
    @Getter
    private final Location spectatorLocation;
    private final int maxPlayersAmountInATeam;
    private final Plugin plugin;
    private final GameTimer gameTimer;
    private final int playerCountToStart;
    private int taskId;

    public Game(Plugin plugin, ConfigContainer configContainer) {
        this.plugin = plugin;
        this.teamContainer = new TeamContainer(configContainer);
        this.itemContainer = new ItemSpawnerContainer(configContainer);
        this.shopInventory = new ShopInventory(configContainer);

        this.gamePhase = GamePhase.START;
        this.gameTimer = new GameTimer();

        this.playerCountToStart = configContainer.getFile("config").getFileConfiguration().getInt("playerCountToStart");
        this.spawnLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spawn");
        this.endLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("end");
        this.spectatorLocation = configContainer.getFile("locations").getFileConfiguration().getLocation("spectator");
        this.maxPlayersAmountInATeam = configContainer.getFile("config").getFileConfiguration().getInt("maxPlayersInATeam");
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
        assignATeamToEachPlayerWithoutATeam();
        teamContainer.spawnEachTeam();
    }

    private void assignATeamToEachPlayerWithoutATeam() {
        String hash;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (teamContainer.isPlayerInTeam(onlinePlayer)) {
                System.out.println("Player is already in a Team");
                continue;
            }
            for (Team team : teamContainer.getTeamList()) {
                hash = team.getName();
                if (team.getNumberOfTeamMember() == maxPlayersAmountInATeam) {
                    System.out.println("Max player in this team");
                    continue;
                }
                if (team.getNumberOfTeamMember() == 0) {
                    team.addTeamMember(onlinePlayer);
                }
                if (teamContainer.getTeamByName(hash).getNumberOfTeamMember() != team.getNumberOfTeamMember()
                        && !(teamContainer.getTeamByName(hash).getNumberOfTeamMember() > team.getNumberOfTeamMember())) {
                    System.out.println("Team weird");
                    continue;
                }
                team.addTeamMember(onlinePlayer);
                break;
            }
        }
    }

    public void stopIfGameIsOver() {
        if (gameHasWinner()) {
            stop();
        }
    }

    private boolean gameHasWinner() {
        return teamContainer.getWinner() != null;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
        gamePhase = GamePhase.END;
        teamContainer.teleportEachTeam(endLocation);
    }
}