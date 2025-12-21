package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.move.Coord;

public abstract class Generator {
    protected static int[][] initialGridWalls(int rows, int columns) {
        int[][] grid = new int[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                grid[y][x] = Constants.WALL;
            }
        }
        return grid;
    }

    protected int[][] initialGridFloors(int rows, int columns) {
        int[][] grid = new int[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                grid[y][x] = Constants.ROOM;
            }
        }
        return grid;
    }

    protected static int randomRow(int rows) {
        return (int) (Math.random() * rows);
    }

    protected static int randomColumn(int columns) {
        return (int) (Math.random() * columns);
    }

    protected static Coord randomCoord(int rows, int columns) {
        return new Coord(randomColumn(columns), randomRow(rows));
    }

    public abstract GridDefinition generate(int rows, int columns);

    public GridDefinition generate() {
        return generate(Constants.ROWS, Constants.COLUMNS);
    }
}
