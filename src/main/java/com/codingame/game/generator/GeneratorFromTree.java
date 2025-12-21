package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.move.Coord;
import com.codingame.game.generator.tree.DungeonTree;
import com.codingame.game.generator.tree.RoomTypes;
import com.codingame.game.move.Direction;

public class GeneratorFromTree extends Generator {
    DungeonTree tree;
    Coord playerStart;
    Coord exitPoint;
    int[][] grid;


    public GeneratorFromTree(DungeonTree dungeonTree) {
        tree = dungeonTree;
    }

    private void placeRoomRec(int x_min, int x_max, int y_min, int y_max, DungeonTree node) {
        // base case, ignore null nodes
        if (node == null) {
            return;
        }

        // get center of the partition
        int centerX = (x_min + x_max) / 2;
        int centerY = (y_min + y_max) / 2;

        // room already placed; todo: let's rethink this...
        if (grid[centerY][centerX] == Constants.ROOM) {
            return;
        }

        // place room in the all available space of partition minus walls; todo: more interesting room shapes
        for (int i = x_min + Constants.WALL_OFFSET; i < x_max - Constants.WALL_OFFSET; i++) {
            for (int j = y_min + Constants.WALL_OFFSET; j < y_max - Constants.WALL_OFFSET; j++) {
                 grid[j][i] = Constants.ROOM;
            }
        }

        // set start and exit points in the center of their rooms; todo: more interesting placement
        if (node.getRoom() instanceof RoomTypes.Start) {
            playerStart = new Coord(centerX, centerY);
        } else if (node.getRoom() instanceof RoomTypes.Exit) {
            exitPoint = new Coord(centerX, centerY);
        }

        // add enemies, items, etc. todo: will be added later

        // place corridors to parent room; todo: also will add more interesting corridor shapes
        if (node.getParentDirection() != null) {
            Direction dir = node.getParentDirection();
            int i, j;
            switch (dir) {
                case RIGHT:
                    i = x_max - Constants.WALL_OFFSET;
                    while (i < grid[0].length && grid[centerY][i] == Constants.WALL) {
                        grid[centerY][i] = Constants.CORRIDOR;
                        i++;
                    }
                    break;
                case LEFT:
                    i = x_min + Constants.WALL_OFFSET - 1;
                    while (i >= 0 && grid[centerY][i] == Constants.WALL) {
                        grid[centerY][i] = Constants.CORRIDOR;
                        i--;
                    }
                    break;
                case UP:
                    j = y_min + Constants.WALL_OFFSET - 1;
                    while (j >= 0 && grid[j][centerX] == Constants.WALL) {
                        grid[j][centerX] = Constants.CORRIDOR;
                        j--;
                    }
                    break;
                case DOWN:
                    j = y_max - Constants.WALL_OFFSET;
                    while (j < grid.length && grid[j][centerX] == Constants.WALL) {
                        grid[j][centerX] = Constants.CORRIDOR;
                        j++;
                    }
                    break;
            }
        }

        // call recursively for children
        int dx = x_max - x_min;
        int dy = y_max - y_min;
        placeRoomRec(x_min -  dx, x_min, y_min, y_max, node.getLeftChild());
        placeRoomRec(x_max, x_max + dx, y_min, y_max, node.getRightChild());
        placeRoomRec(x_min, x_max, y_min - dy, y_min, node.getTopChild());
        placeRoomRec(x_min, x_max, y_max, y_max + dy, node.getBottomChild());
    }

    @Override
    public GridDefinition generate(int rows, int columns) {
        grid = Generator.initialGridWalls(rows, columns);

        int treeWidth = tree.getTreeWidth();
        int treeHeight = tree.getTreeHeight();

        // calculating partitions; todo: cutting branches with moving exit up if cut off
        int partitionWidth = columns / treeWidth;
        if (partitionWidth < Constants.MIN_PARTITION_DIMENSION) {
            throw new IllegalArgumentException("Grid too small for the generated tree structure (width)");
        }
        int partitionHeight = rows / treeHeight;
        if (partitionHeight < Constants.MIN_PARTITION_DIMENSION) {
            // todo: cutting branches or sth
            throw new IllegalArgumentException("Grid too small for the generated tree structure (height)");
        }

        int placeholderLeft = tree.getSpaceLeft() * partitionWidth;
        int placeholderTop = tree.getSpaceTop() * partitionHeight;

        // place rooms
        placeRoomRec(
                placeholderLeft,
                placeholderLeft + partitionWidth,
                placeholderTop,
                placeholderTop + partitionHeight,
                tree
        );

        return GridDefinition.builder()
                .grid(grid)
                .playerStart(playerStart)
                .exit(exitPoint)
                .build();
    }
}
