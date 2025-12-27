package com.codingame.game.game_objects;

import com.codingame.game.move.Coord;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GamePlayer extends GameObject {
    private static final int MAX_STAMINA = 10;

    private int lives = 1;
    private int score = 0;
    private int stamina = MAX_STAMINA;

    public GamePlayer(Coord position) {
        super(10, "player/", position);
    }

    public Void addScore() {
        this.score++;
        return null;
    }

    public Void addLife() {
        this.lives++;
        return null;
    }

    public void decreaseScore() {
        this.score--;
    }

    public boolean decreaseLife() {
        this.lives--;
        return this.lives <= 0;
    }
}
