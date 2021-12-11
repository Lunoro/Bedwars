package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.config.ConfigContainer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamContainer {

    @Getter
    private final List<Team> teamList;
    private final TeamLoader teamLoader;

    public TeamContainer(ConfigContainer configContainer) {
        Config teamsConfig = configContainer.getFile("teams");
        this.teamLoader = new TeamLoader(teamsConfig);
        this.teamList = teamLoader.loadTeams();
    }

    public void save() {
        teamLoader.save(teamList);
    }

    public void addTeam(Team team) {
        teamList.add(team);
    }

    public void removeTeam(Team team) {
        teamList.add(team);
    }

    public Team getWinner() {
        int i = 0;
        for (Team team : teamList) {
            if (!team.entireTeamIsDead() && i == teamList.size() - 1) {
                return team;
            }
            i++;
        }
        return null;
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
            if (team.playerIsInThisTeam(player)) {
                return team;
            }
        }
        return null;
    }

    public boolean playerHasTeam(Player player) {
        return getTeamOfPlayer(player) != null;
    }

    public Team getTeamByName(String name) {
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
