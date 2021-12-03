package de.lunoro.bedwars.game.team;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team {

    private final List<TeamMember> memberList;
    private final ChatColor color;
    private final Location spawnLocation;
    private final Location bedLocation;

    public Team(ChatColor color, Location spawnLocation, Location bedLocation) {
        memberList = new ArrayList<>();
        this.bedLocation = bedLocation;
        this.color = color;
        this.spawnLocation = spawnLocation;
    }

    public void spawnTeam() {
        teleportTeam(spawnLocation);
    }

    public void teleportTeam(Location location) {
        for (TeamMember teamMember : memberList) {
            teamMember.getPlayer().teleport(location);
        }
    }

    public boolean hasBed() {
        return bedLocation.getBlock().getType().name().contains("bed");
    }

    public void addTeamMember(Player player) {
        memberList.add(new TeamMember(player));
    }

    public void removeTeamMember(Player player) {
        memberList.remove(getTeamMember(player));
    }

    public TeamMember getTeamMember(Player player) {
        for (TeamMember teamMember : memberList) {
            if (teamMember.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return teamMember;
            }
        }
        return null;
    }
}
