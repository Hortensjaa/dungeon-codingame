package com.codingame.game;


public class Constants {
//    grid dimensions
    public static final int COLUMNS = 30;
    public static final int ROWS = 17;
    public static final int CELL_SIZE = 64;
    public static final int VIEWER_WIDTH = COLUMNS * CELL_SIZE;
    public static final int VIEWER_HEIGHT = ROWS * CELL_SIZE;

//    grid cell types
    public static final int WALL = 0;
    public static final int FLOOR = 1;

//    sprites
    public static final String WALL_SPRITE = "assets/walls/tile_8.png";
    public static final String FLOOR_SPRITE = "assets/walls/tile_10.png";
    public static final String PLAYER_SPRITE = "player/player.png";
    public static final String GOAL_SPRITE = "walls/tile_6.png";

//    actions
    public static final String[] ACTIONS = {
            "UP", "DOWN", "LEFT", "RIGHT", "STAY"
    };
}
