package com.codingame.game;

public enum Action {

    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    STAY(0, 0);

    private final int x;
    private final int y;

    Action(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

