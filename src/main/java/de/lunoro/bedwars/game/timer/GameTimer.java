package de.lunoro.bedwars.game.timer;

import de.lunoro.bedwars.game.IGameObject;
import lombok.Getter;

public class GameTimer implements IGameObject {

    @Getter
    private int timer = -10;

    @Override
    public void update(int gameTick) {
        timer++;
    }
}
