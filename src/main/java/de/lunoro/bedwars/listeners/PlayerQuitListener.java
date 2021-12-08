package de.lunoro.bedwars.listeners;

import de.lunoro.bedwars.game.team.Team;
import de.lunoro.bedwars.game.team.TeamContainer;
import de.lunoro.bedwars.game.team.teammember.TeamMember;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final TeamContainer teamContainer;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Team team = teamContainer.getTeamOfPlayer(player);

        if (team == null) {
            return;
        }

        TeamMember teamMember = team.getTeamMember(player);
        event.setQuitMessage(ChatColor.RED + "🚪 " + ChatColor.WHITE + event.getPlayer().getDisplayName());
        teamMember.switchRespawn();
    }
}
