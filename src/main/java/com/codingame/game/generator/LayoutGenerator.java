package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.move.Direction;
import com.codingame.game.tree.DungeonTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LayoutGenerator {

    private static final Random RANDOM = new Random();

    public static boolean generateLayout(DungeonTree node, LayoutField[][] grid, int x, int y, Direction directionFromParent) {
        int width = grid[0].length;
        int height = grid.length;

        if (grid[y][x] != null) return false;

        grid[y][x] = new LayoutField(node.getType(), directionFromParent);

        List<DungeonTree> children = new ArrayList<>(node.getChildren());
        Collections.shuffle(children, RANDOM);

        for (DungeonTree child : children) {
            List<Direction> directions = Direction.shuffledDirections();
            boolean placed = false;

            for (Direction dir : directions) {
                int nx = x + dir.getDx();
                int ny = y + dir.getDy();

                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (grid[ny][nx] == null) {
                        if (generateLayout(child, grid, nx, ny, dir.opposite())) {
                            placed = true;
                            break;
                        }
                    }
                }
            }

            if (!placed) {
                grid[y][x] = null;
                return false;
            }
        }

        return true;
    }

    private static LayoutField[][] trim(LayoutField[][] layout) {
        int h = layout.length;
        int w = layout[0].length;

        int minX = w, minY = h;
        int maxX = -1, maxY = -1;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (layout[y][x] != null) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX == -1) {
            return new LayoutField[0][0];
        }

        int newW = maxX - minX + 1;
        int newH = maxY - minY + 1;

        LayoutField[][] trimmed = new LayoutField[newH][newW];

        for (int y = 0; y < newH; y++) {
            System.arraycopy(layout[y + minY], minX, trimmed[y], 0, newW);
        }

        return trimmed;
    }


    public static LayoutField[][] generateLayout(DungeonTree root) throws IllegalArgumentException{
        LayoutField[][] grid = new LayoutField[Constants.MAX_LAYOUT_HEIGHT][Constants.MAX_LAYOUT_WIDTH];

        int startX = Constants.MAX_LAYOUT_WIDTH / 2;
        int startY = Constants.MAX_LAYOUT_HEIGHT / 2;

        boolean success = generateLayout(root, grid, startX, startY, null);

        if (!success) {
            throw new IllegalArgumentException("Failed to generate layout, try again.");
        }

        return trim(grid);
    }

//    ------------------ printer -----------------------
    public static void printLayout(LayoutField[][] layout) {
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[0].length; x++) {
                if (layout[y][x] != null) {
                    System.out.print(layout[y][x].toString());
                } else {
                    System.out.print("[ ] ");
                }
            }
            System.out.println();
        }
    }
}
