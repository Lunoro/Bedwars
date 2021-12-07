package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.config.Config;
import de.lunoro.bedwars.game.spawner.ItemSpawner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TeamLoader {

    private final Config config;

    public List<Team> loadTeams() {
        List<Team> list = new ArrayList<>();
        FileConfiguration teamsFileConfig = config.getFileConfiguration();
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

    public void save(List<Team> teamList) {
        int i = 0;
        config.clear();
        for (Team team : teamList) {
            saveInConfigurationSection(team, config.getFileConfiguration().createSection(String.valueOf(i)));
            i++;
        }
        config.save();
    }

    private void saveInConfigurationSection(Team team, ConfigurationSection configurationSection) {
        configurationSection.set("name", team.getName());
        configurationSection.set("color", team.getColorCode());
        configurationSection.set("spawnlocation", team.getSpawnLocation());
        configurationSection.set("bedlocation", team.getBedLocation());
    }
}
