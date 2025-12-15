package com.codingame.game.generators;

import com.codingame.game.Constants;
import com.codingame.game.Coord;

public class RandomGenerator extends Generator {
    @Override
    public GridDefinition generate(int rows, int columns) {
        int[][] grid = initialGridFloors(rows, columns);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (Math.random() < 0.1) {
                    grid[y][x] = Constants.WALL; // place wall
                }
            }
        }
        Coord player_coords = randomCoord(rows, columns);
        Coord goal_coords = randomCoord(rows, columns);
        GridDefinition definition =  GridDefinition.builder()
                .grid(grid)
                .playerStart(player_coords)
                .exit(goal_coords)
                .build();
        definition.setCoordValue(player_coords, Constants.FLOOR);
        definition.setCoordValue(goal_coords, Constants.FLOOR);
        return definition;
    }
}
