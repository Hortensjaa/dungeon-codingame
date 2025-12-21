package com.codingame.game;

import com.codingame.game.game_objects.GamePlayer;
import com.codingame.game.generator.GridDefinition;
import com.codingame.game.move.Action;
import com.codingame.game.move.Coord;
import lombok.Getter;


@Getter
public class DungeonGame {

    private final int[][] grid;
    private GamePlayer player;
    private final Coord exit;

    public DungeonGame(GridDefinition def) {
        this.grid = def.getGrid();
        this.player = new GamePlayer(def.getPlayerStart());
        this.exit = def.getExit();
    }

    public boolean move(Action action) {
        Coord next = player.getPosition().applyAction(action);

        if (isOutOfBounds(next)) return false;
        if (isWall(next)) return false;

        player.setPosition(next);
        return true;
    }

    public boolean hasWon() {
        return player.getPosition().equals(exit);
    }

    private boolean isOutOfBounds(Coord c) {
        return c.getX() < 0 || c.getY() < 0
                || c.getX() >= Constants.COLUMNS
                || c.getY() >= Constants.ROWS;
    }

    private boolean isWall(Coord c) {
        return grid[c.getY()][c.getX()] == Constants.WALL;
    }
}
