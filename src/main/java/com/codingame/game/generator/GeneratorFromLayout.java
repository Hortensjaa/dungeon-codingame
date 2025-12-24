package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.game_objects.EnemyType;
import com.codingame.game.move.Coord;
import com.codingame.game.move.Direction;
import com.codingame.game.tree.NodeTypes;

import java.util.HashMap;
import java.util.Map;

public class GeneratorFromLayout extends Generator {
    private final LayoutField[][] layout;
    private Coord playerStart;
    private Coord exitPoint;
    private final Map<Coord, EnemyType> enemies = new HashMap<>();
    private int[][] grid;

    private final int trimmedH;
    private final int trimmedW;
    private final int partitionWidth;
    private final int partitionHeight;

    public GeneratorFromLayout(LayoutField[][] layout) {
        this.layout = layout;
        trimmedH = layout.length;
        trimmedW = layout[0].length;
        partitionWidth = Constants.COLUMNS / trimmedW;
        partitionHeight = Constants.ROWS / trimmedH;
    }

    // Converts layout coordinates to grid coordinates (left upper corner of the partition)
    private Coord toGridCoords(Coord layoutCoord) {
        return new Coord(layoutCoord.getX() * partitionWidth, layoutCoord.getY() * partitionHeight);
    }

    private void placeRoom(Coord leftUpperCorner) {
        int startX = leftUpperCorner.getX() + Constants.WALL_OFFSET;
        int startY = leftUpperCorner.getY() + Constants.WALL_OFFSET;
        int endX = startX + partitionWidth - 2 * Constants.WALL_OFFSET;
        int endY = startY + partitionHeight - 2 * Constants.WALL_OFFSET;

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                grid[y][x] = Constants.ROOM;
            }
        }
    }

    private void placeCorridor(Coord center, Direction direction) {
        int x = center.getX();
        int y = center.getY();

        boolean inCurRoom = true;
        while (x >= 0 && x < Constants.COLUMNS && y >= 0 && y < Constants.ROWS) {
            if (!inCurRoom && grid[y][x] != Constants.WALL) break;
            if (grid[y][x] == Constants.WALL) {
                grid[y][x] = Constants.CORRIDOR;
                inCurRoom = false;
            }
            x += direction.getDx();
            y += direction.getDy();
        }
    }

    private void placeRooms() {
        grid = Generator.initialGridWalls(Constants.ROWS, Constants.COLUMNS);

        for (int y = 0; y < trimmedH; y++) {
            for (int x = 0; x < trimmedW; x++) {
                LayoutField field = layout[y][x];
                if (field == null) continue;

                Coord leftUpperCorner = toGridCoords(new Coord(x, y));
                placeRoom(leftUpperCorner);

                int centerX = leftUpperCorner.getX() + partitionWidth / 2;
                int centerY = leftUpperCorner.getY() + partitionHeight / 2;
                Coord center = new Coord(centerX, centerY);

                // Track special rooms
                if (field.type instanceof NodeTypes.Start) {
                    playerStart = center;
                } else if (field.type instanceof NodeTypes.Exit) {
                    exitPoint = center;
                } else if (field.type instanceof NodeTypes.Enemies) {
                    enemies.put(center, EnemyType.FIRE);
                }
            }
        }
    }

    private void placeCorridors() {
        for (int y = 0; y < trimmedH; y++) {
            for (int x = 0; x < trimmedW; x++) {
                LayoutField field = layout[y][x];
                if (field == null) continue;

                Coord leftUpperCorner = toGridCoords(new Coord(x, y));

                int centerX = leftUpperCorner.getX() + partitionWidth / 2;
                int centerY = leftUpperCorner.getY() + partitionHeight / 2;
                Coord center = new Coord(centerX, centerY);

                if (field.parentDirection != null) placeCorridor(center, field.parentDirection);
            }
        }
    }

    @Override
    public GridDefinition generate(int rows, int columns) {
        placeRooms();
        placeCorridors();

        return GridDefinition.builder()
                .grid(grid)
                .playerStart(playerStart)
                .exit(exitPoint)
                .enemies(enemies)
                .build();
    }
}
