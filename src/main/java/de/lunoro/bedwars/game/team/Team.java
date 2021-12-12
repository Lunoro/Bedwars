package de.lunoro.bedwars.game.team;

import de.lunoro.bedwars.game.team.teammember.TeamMember;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Type;
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
    private Location bedLocation;
    @Getter
    private boolean isEliminated;

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

    public boolean hasBed() {
        return bedLocation.getBlock().getType().name().contains("BED");
    }

    public boolean playerIsInThisTeam(Player player) {
        return getTeamMember(player) != null;
    }

    public TeamMember getTeamMember(Player player) {
        for (TeamMember teamMember : memberList) {
            if (teamMember.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return teamMember;
            }
        }
        return null;
    }

    public void addTeamMember(Player player) {
        if (!playerIsInThisTeam(player)) {
            player.setDisplayName(color + player.getName() + "");
            scoreBoardTeam.addEntry(player.getName());
            memberList.add(new TeamMember(player));
        }
    }

    public void removeTeamMember(Player player) {
        player.setDisplayName(ChatColor.WHITE + player.getName());
        memberList.remove(getTeamMember(player));
    }

    public void updateTeamStatus() {
        for (TeamMember teamMember : memberList) {
            if (teamMember.isRespawnable()) {
                return;
            }
        }
        isEliminated = true;
    }

    public void setBedLocation(Location bedLocation) {
        this.bedLocation = bedLocation;
    }

    public int getTeamSize() {
        return memberList.size();
    }

    public void unregisterScoreboardTeam() {
        scoreBoardTeam.unregister();
    }
}
