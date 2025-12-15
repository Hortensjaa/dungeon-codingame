package com.codingame.game.generators;

import com.codingame.game.Coord;

public class RandomGenerator extends Generator {
    @Override
    public GridDefinition generate(int rows, int columns) {
        int[][] grid = initialGridFloors(rows, columns);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (Math.random() < 0.1) {
                    grid[y][x] = 0; // place wall
                }
            }
        }
        Coord player_coords = new Coord(randomColumn(columns), randomRow(rows));
        Coord goal_coords = new Coord(randomColumn(columns), randomRow(rows));
        grid[player_coords.getY()][player_coords.getX()] = 1;
        grid[goal_coords.getY()][goal_coords.getX()] = 1;
        return GridDefinition.builder()
                .grid(grid)
                .playerStart(player_coords)
                .goal(goal_coords)
                .build();
    }
}
