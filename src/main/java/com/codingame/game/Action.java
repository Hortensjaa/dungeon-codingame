package com.codingame.game;

import lombok.Getter;

@Getter
public enum Action {

    UP(0, -1, "UP"),
    DOWN(0, 1, "DOWN"),
    LEFT(-1, 0, "LEFT"),
    RIGHT(1, 0, "RIGHT"),
    STAY(0, 0, "STAY");

    private final int x;
    private final int y;
    private final String name;

    Action(int x, int y, String name) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Action opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case STAY:
                return STAY;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int length() {
        return Math.abs(x) + Math.abs(y);
    }
}

