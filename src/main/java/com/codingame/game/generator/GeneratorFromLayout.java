package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.game_objects.EnemyType;
import com.codingame.game.move.Coord;
import com.codingame.game.move.Direction;
import com.codingame.game.tree.DungeonTree;
import com.codingame.game.tree.DungeonTreeSerializer;
import com.codingame.game.tree.NodeTypes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This class generates a dungeon layout from a given layout or dungeon tree.
 * It handles the placement of rooms, corridors, and special elements like the player start,
 * exit point, and enemies.
 */
public class GeneratorFromLayout extends Generator {
    private final LayoutField[][] layout; // The layout of the dungeon as a 2D array of fields
    private Coord playerStart; // The starting position of the player
    private Coord exitPoint; // The exit point of the dungeon
    private final Map<Coord, EnemyType> enemies = new HashMap<>(); // Map of enemy positions and their types
    private int[][] grid; // The grid representation of the dungeon

    private final int trimmedH; // Height of the trimmed layout
    private final int trimmedW; // Width of the trimmed layout
    private final int partitionWidth; // Width of each partition in the grid
    private final int partitionHeight; // Height of each partition in the grid

    /**
     * Constructor for GeneratorFromLayout.
     *
     * @param layout The layout of the dungeon as a 2D array of LayoutField objects.
     */
    public GeneratorFromLayout(LayoutField[][] layout) {
        this.layout = layout;
        trimmedH = layout.length;
        trimmedW = layout[0].length;
        partitionWidth = Constants.COLUMNS / trimmedW;
        partitionHeight = Constants.ROWS / trimmedH;
    }

    /**
     * Converts layout coordinates to grid coordinates.
     *
     * @param layoutCoord The coordinates in the layout.
     * @return The corresponding coordinates in the grid.
     */
    private Coord toGridCoords(Coord layoutCoord) {
        return new Coord(layoutCoord.getX() * partitionWidth, layoutCoord.getY() * partitionHeight);
    }

    /**
     * Places a room in the grid at the specified top-left corner.
     *
     * @param leftUpperCorner The top-left corner of the room in the grid.
     */
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

    /**
     * Places a corridor in the grid starting from the center and extending in the given direction.
     *
     * @param center    The starting point of the corridor.
     * @param direction The direction in which the corridor extends.
     */
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

    /**
     * Places all rooms in the grid based on the layout.
     * Tracks special rooms like the player start, exit, and enemy positions.
     */
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

    /**
     * Places all corridors in the grid based on the layout.
     */
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

    // ----------------- API -----------------

    /**
     * Generates a dungeon grid from a DungeonTree.
     *
     * @param tree The DungeonTree to generate the dungeon from.
     * @return A GridDefinition object representing the generated dungeon.
     */
    public static GridDefinition generate(DungeonTree tree, int maxRetries) {
        LayoutField[][] layout = LayoutGenerator.generateLayout(tree, maxRetries);
        LayoutGenerator.printLayout(layout);

        GeneratorFromLayout generator = new GeneratorFromLayout(layout);

        generator.placeRooms();
        generator.placeCorridors();

        return GridDefinition.builder()
                .grid(generator.grid)
                .playerStart(generator.playerStart)
                .exit(generator.exitPoint)
                .enemies(generator.enemies)
                .build();
    }

    /**
     * Generates a dungeon grid from a file.
     *
     * @param folder The name of the file containing the DungeonTree.
     * @param x      The x coordinate of the layout file.
     * @param y      The y coordinate of the layout file.
     * @return A GridDefinition object representing the generated dungeon.
     * @throws RuntimeException If the file cannot be read or the dungeon cannot be generated.
     */
    public static GridDefinition generate(String folder, int x, int y, int maxRetries) {
        String filename = "levels/" + folder + "/x_" + String.format("%02d", x) + "_y_" + String.format("%02d", y) + ".json";
        File file = new File(filename);
        try {
            DungeonTree tree = DungeonTreeSerializer.readFromFile(file);
            return generate(tree, maxRetries);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate from file: " + filename, e);
        }
    }
}
