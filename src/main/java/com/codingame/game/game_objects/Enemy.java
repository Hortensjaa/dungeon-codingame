package com.codingame.game.game_objects;

import com.codingame.game.move.Coord;

public class Enemy extends GameObject {
    public Enemy(EnemyType type, Coord position) {
        super(type.getFrames(), type.getFilePrefix(), position);
    }

    public void attack(GamePlayer player) {
        player.decreaseScore(); // todo: in standalone game it will be health decrease
    }
}
