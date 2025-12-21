package com.codingame.game.game_objects;

import com.codingame.game.move.Coord;


public class GamePlayer extends GameObject {
    public GamePlayer(Coord position) {
        super(10, "player/", position);
    }
}
