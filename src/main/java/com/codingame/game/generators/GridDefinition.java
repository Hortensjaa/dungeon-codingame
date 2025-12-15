package com.codingame.game.generators;

import com.codingame.game.Constants;
import com.codingame.game.Coord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GridDefinition {

    @Builder.Default
    private final int[][] grid = new int[Constants.ROWS][Constants.COLUMNS];

    @Builder.Default
    private final Coord playerStart = new Coord(0, 0);

    @Builder.Default
    private final Coord exit = new Coord(Constants.COLUMNS - 1, Constants.ROWS - 1);

    @Builder.Default
    private final Coord[] enemies = new Coord[0];

    public int rows() {
        return grid.length;
    }
    public int columns() {
        return grid[0].length;
    }

    public int getCoordValue(Coord coord) {
        return grid[coord.getY()][coord.getX()];
    }

    public void setCoordValue(Coord coord, int value) {
        grid[coord.getY()][coord.getX()] = value;
    }
}

