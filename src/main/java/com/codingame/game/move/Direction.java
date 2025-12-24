package com.codingame.game.move;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction implements Move {
    UP(0, -1, "UP"),
    DOWN(0, 1, "DOWN"),
    LEFT(-1, 0, "LEFT"),
    RIGHT(1, 0, "RIGHT");

    private final int dx;
    private final int dy;
    private final String name;

    public Direction opposite() {
        if (this == UP) return DOWN;
        if (this == DOWN) return UP;
        if (this == LEFT) return RIGHT;
        if (this == RIGHT) return LEFT;
        return this;
    }

    public static Direction fromString(String s) {
        for (Direction d : values()) {
            if (d.name.equalsIgnoreCase(s)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Unknown direction: " + s);
    }
}

