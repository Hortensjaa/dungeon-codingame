package com.codingame.game;

public class GamePlayer {
    private final int FRAMES_COUNT = 10;
    private int animationFrame = 0;

    public String getCurrentSprite() {
        animationFrame++;
        animationFrame = animationFrame % FRAMES_COUNT;
        return "player/tile_" + animationFrame + ".png";
    }
}
