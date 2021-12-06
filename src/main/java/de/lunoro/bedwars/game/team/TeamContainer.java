package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamContainer {

    private final List<Team> teamList;
    private final ConfigContainer configContainer;

    public TeamContainer(ConfigContainer configContainer) {
        this.configContainer = configContainer;
        this.teamList = loadTeams();
    }

    private List<Team> loadTeams() {
        List<Team> list = new ArrayList<>();
        FileConfiguration teamsFileConfig = configContainer.getFile("teams").getFileConfiguration();
        for (String key : teamsFileConfig.getKeys(false)) {
            String name = teamsFileConfig.getConfigurationSection(key).getString("name");
            char color = teamsFileConfig.getConfigurationSection(key).getString("color").charAt(0);
            Location spawn = teamsFileConfig.getConfigurationSection(key).getLocation("spawnlocation");
            Location bedLocation = teamsFileConfig.getConfigurationSection(key).getLocation("bedlocation");
            Team team = new Team(name, color, spawn, bedLocation);
            list.add(team);
        }
        return list;
    }

    public void save() {
        int i = 0;
        Config teamsConfig = configContainer.getFile("teams");
        teamsConfig.clear();
        for (Team team : teamList) {
            saveInConfigurationSection(team, teamsConfig.getFileConfiguration().createSection(String.valueOf(i)));
            i++;
        }
        teamsConfig.save();
    }

    private void saveInConfigurationSection(Team team, ConfigurationSection configurationSection) {
        configurationSection.set("name", team.getName());
        configurationSection.set("color", team.getColorCode());
        configurationSection.set("spawnlocation", team.getSpawnLocation());
        configurationSection.set("bedlocation", team.getBedLocation());
    }

    public void addTeam(Team team) {
        teamList.add(team);
    }

    public void removeTeam(Team team) {
        teamList.add(team);
    }

    public Team getWinner() {
        Team winner = null;
        int i = 0;
        for (Team team : teamList) {
            if (!team.entireTeamIsDead()) {
                i++;
            }
            if (i == 1) {
                winner = team;
            }
        }
        return winner;
    }

    public void spawnEachTeam() {
        for (Team team : teamList) {
            team.spawnTeam();
        }
    }

    public void teleportEachTeam(Location location) {
        for (Team team : teamList) {
            team.teleportTeam(location);
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

    public Team getTeamByName(String name) {
        System.out.println(teamList);
        for (Team team : teamList) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public void unregisterAllScoreboardTeams() {
        for (Team team : teamList) {
            team.unregisterScoreboardTeam();
        }
    }
}
