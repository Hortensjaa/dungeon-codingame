package com.codingame.game;


public class Constants {
//    grid dimensions
    public static final int COLUMNS = 60;
    public static final int ROWS = 34;
    public static final int CELL_SIZE = 32;
    public static final int VIEWER_WIDTH = COLUMNS * CELL_SIZE;
    public static final int VIEWER_HEIGHT = ROWS * CELL_SIZE;

//    grid cell types
    public static final int WALL = 0;
    public static final int ROOM = 1;
    public static final int CORRIDOR = 2;

//  grid constants (doesn't depend on CELL_SIZE)
    public static final int WALL_OFFSET = 1; // in cells
    public static final int CORRIDOR_WIDTH = 1; // in cells
    public static final int MIN_ROOM_DIMENSION_X = 5; // minimum room width and height in cells
    public static final int MIN_ROOM_DIMENSION_Y = 3;
    public static final int MIN_PARTITION_WIDTH = MIN_ROOM_DIMENSION_X + 2 * WALL_OFFSET; // minimum partition width and height in cells
    public static final int MIN_PARTITION_HEIGHT = MIN_ROOM_DIMENSION_Y + 2 * WALL_OFFSET; // minimum partition width and height in cells

//    tree constants
    public static final int MAX_LAYOUT_WIDTH = COLUMNS / MIN_PARTITION_WIDTH;
    public static final int MAX_LAYOUT_HEIGHT = ROWS / MIN_PARTITION_HEIGHT;
    public static final int MIN_DEPTH = 2;
    public static final int MAX_DEPTH = 5;
    public static final int MAX_NODES = MAX_LAYOUT_HEIGHT * MAX_LAYOUT_WIDTH;

//    rooms constraints
    public static final float MAX_ENEMY_COVERAGE = 0.25f;
    public static final float MAX_REWARD_COVERAGE = 0.25f;

//    sprites
    public static final String WALL_SPRITE = "assets/walls_32/tile_10.png";
    public static final String ROOM_SPRITE = "assets/walls_32/tile_11.png";
    public static final String CORRIDOR_SPRITE = "assets/walls_32/tile_2.png";
    public static final String GOAL_SPRITE = "assets/walls_32/tile_6.png";

//    actions
    public static final String[] ACTIONS = {
            "UP", "DOWN", "LEFT", "RIGHT", "STAY"
    };
}
