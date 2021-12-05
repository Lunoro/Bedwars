package de.lunoro.bedwars.game.team.teammember;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class TeamMember {

    private final Player player;
    private boolean respawnable;

    public TeamMember(Player player) {
        this.player = player;
        respawnable = true;
    }

    public void switchRespawn() {
        respawnable = false;
    }
}
