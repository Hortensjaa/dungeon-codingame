package com.codingame.game.game_objects;

import com.codingame.game.move.Action;
import com.codingame.game.move.Coord;
import lombok.Getter;
import lombok.Setter;

public class Enemy extends GameObject {
    @Setter @Getter private Action action;
    private final int slowdown = 2; // move once every 'slowdown' turns
    private int turnCounter = 0;

    public Enemy(EnemyType type, Coord position) {
        super(type.getFrames(), type.getFilePrefix(), position);
        if (type.isMoving()) {
            this.action = Math.random() < 0.5 ? Action.LEFT : Action.UP;
        } else {
            this.action = Action.STAY;
        }
    }

    @Override
    public Coord move(Action action) {
        if (action == Action.STAY) {
            return getPosition();
        }
        turnCounter = (turnCounter + 1) % slowdown;
        if (turnCounter == 0) {
            return super.move(action);
        } else {
            return getPosition();
        }
    }

    public void attack(GamePlayer player) {
        player.decreaseScore(); // todo: in standalone game it will be health decrease
    }
}
