package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.game.team.teammember.TeamMember;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Team {

    private final String name;
    private final List<TeamMember> memberList;
    private final char colorCode;
    private final ChatColor color;
    private org.bukkit.scoreboard.Team scoreBoardTeam;
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
        initScoreboardTeam();

        memberList = new ArrayList<>();
    }

    public Team(String name, char colorCode, Location spawnLocation, Location bedLocation) {
        this.name = name;
        this.colorCode = colorCode;
        this.spawnLocation = spawnLocation;
        this.bedLocation = bedLocation;
        this.color = ChatColor.getByChar(colorCode);
        initScoreboardTeam();

        memberList = new ArrayList<>();

    }

    private void initScoreboardTeam() {
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreBoardTeam = scoreboard.getTeam(name);
        if (scoreBoardTeam == null) {
            scoreBoardTeam = scoreboard.registerNewTeam(name);
        }
        scoreBoardTeam.setColor(color);
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
        return bedLocation.getBlock().getType().name().contains("BED");
    }

    public void addTeamMember(Player player) {
        player.setDisplayName(color + player.getName() + "");
        scoreBoardTeam.addEntry(player.getName());
        memberList.add(new TeamMember(player));
    }

    public void removeTeamMember(Player player) {
        player.setDisplayName(ChatColor.WHITE + player.getName());
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

    public int getNumberOfTeamMember() {
        return memberList.size();
    }

    public void unregisterScoreboardTeam() {
        scoreBoardTeam.unregister();
    }
}
