package com.codingame.game;

import com.codingame.game.generators.GridDefinition;
import lombok.Getter;


@Getter
public class DungeonGame {

    private final int[][] grid;
    private Coord playerPos;
    private final Coord goal;

    public DungeonGame(GridDefinition def) {
        this.grid = def.getGrid();
        this.playerPos = def.getPlayerStart();
        this.goal = def.getGoal();
    }

    public boolean move(Action action) {
        Coord next = playerPos.applyAction(action);

        if (isOutOfBounds(next)) return false;
        if (isWall(next)) return false;

        playerPos = next;
        return true;
    }

    public boolean hasWon() {
        return playerPos.equals(goal);
    }

    private boolean isOutOfBounds(Coord c) {
        return c.getX() < 0 || c.getY() < 0
                || c.getX() >= Constants.COLUMNS
                || c.getY() >= Constants.ROWS;
    }

    private boolean isWall(Coord c) {
        return grid[c.getY()][c.getX()] != Constants.FLOOR;
    }
}
