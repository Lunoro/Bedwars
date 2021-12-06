package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.game.team.teammember.TeamMember;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team {

    private final String name;
    private final List<TeamMember> memberList;
    private final char colorCode;
    private final ChatColor color;
    @Setter
    private Location spawnLocation;
    @Setter
    private Location bedLocation;

    public Team(String name, char colorCode) {
        this.name = name;
        this.colorCode = colorCode;
        this.color = ChatColor.getByChar(colorCode);
        this.spawnLocation = null;
        this.bedLocation = null;

        memberList = new ArrayList<>();
    }

    public Team(String name, char colorCode, Location spawnLocation, Location bedLocation) {
        this.name = name;
        this.colorCode = colorCode;
        this.spawnLocation = spawnLocation;
        this.bedLocation = bedLocation;
        this.color = ChatColor.getByChar(colorCode);

        memberList = new ArrayList<>();
    }

    public void spawnTeam() {
        teleportTeam(spawnLocation);
    }

    public void teleportTeam(Location location) {
        for (TeamMember teamMember : memberList) {
            teamMember.getPlayer().teleport(location);
        }
    }

    public boolean entireTeamIsDead() {
        for (TeamMember teamMember : memberList) {
            if (teamMember.isRespawnable()) {
                return false;
            }
        }
        return true;
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
