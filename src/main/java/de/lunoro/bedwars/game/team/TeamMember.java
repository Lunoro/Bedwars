package de.lunoro.bedwars.game.team;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class TeamMember {

    private final Player player;
    private boolean canRespawn;

    public TeamMember(Player player) {
        this.player = player;
        canRespawn = true;
    }

    public void switchRespawn() {
        canRespawn = false;
    }
}
