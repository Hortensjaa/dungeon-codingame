package com.codingame.game.move;

public interface Move {
    int getDx();
    int getDy();

    default int length() {
        return Math.abs(getDx()) + Math.abs(getDy());
    }

    default boolean isStationary() {
        return getDx() == 0 && getDy() == 0;
    }
}

