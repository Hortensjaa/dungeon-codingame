package com.codingame.game.game_objects;

import com.codingame.game.move.Coord;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameObject {
    private final int FRAMES_COUNT;
    private final String FILE_PREFIX;
    private int animationFrame = 0;
    @Setter
    private Coord position;

    public GameObject(int framesCount, String prefix, Coord position) {
        this.FRAMES_COUNT = framesCount;
        this.FILE_PREFIX = prefix;
        this.position = position;
    }

    public String getCurrentSprite() {
        animationFrame++;
        animationFrame = animationFrame % FRAMES_COUNT;
        return FILE_PREFIX + animationFrame + ".png";
    }
}
