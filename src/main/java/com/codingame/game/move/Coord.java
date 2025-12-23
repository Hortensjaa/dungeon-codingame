package com.codingame.game.move;

import lombok.Getter;

@Getter
public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord applyAction(Action action) {
        int new_x = this.x + action.getDx();
        int new_y = this.y + action.getDy();
        return new Coord(new_x, new_y);
    }

    public Coord add(Coord other) {
        return new Coord(this.x + other.x, this.y + other.y);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
