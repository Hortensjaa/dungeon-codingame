package com.codingame.game;

import java.util.Arrays;
import java.util.List;

import com.codingame.game.generators.BinarySpacePartitioning;
import com.codingame.game.generators.GridDefinition;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.google.inject.Inject;


/**
 * Referee for the Dungeon game.
 * 
 * <h2>Input Protocol</h2>
 * 
 * <h3>Initialization Input (sent once at start):</h3>
 * <pre>
 * Line 1: WIDTH HEIGHT (grid dimensions, space-separated integers)
 * Line 2: EXIT_X EXIT_Y (exit/goal position, space-separated integers)
 * Next HEIGHT lines: ROW (string of 0s and 1s, where 0=wall, 1=floor)
 * </pre>
 * 
 * <h3>Turn Input (sent each turn):</h3>
 * <pre>
 * Line 1: PLAYER_X PLAYER_Y (current player position, space-separated integers)
 * </pre>
 * 
 * <h3>Expected Output (each turn):</h3>
 * <pre>
 * One of: UP, DOWN, LEFT, RIGHT, STAY
 * </pre>
 */
public class Referee extends AbstractReferee {

    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private DungeonGame game;
    private Sprite playerSprite;
    private Sprite goalSprite;

    private void drawGrid(int[][] grid) {
        // Draw background color (floor color)
        graphicEntityModule.createRectangle()
                .setWidth(Constants.VIEWER_WIDTH)
                .setHeight(Constants.VIEWER_HEIGHT)
                .setFillColor(0x3d3d3d)
                .setZIndex(-1);

        // Draw only walls
        for (int y = 0; y < Constants.ROWS; y++) {
            for (int x = 0; x < Constants.COLUMNS; x++) {
                if (grid[y][x] == Constants.WALL) {
                    graphicEntityModule.createSprite()
                            .setImage(Constants.WALL_SPRITE)
                            .setScale(0.5)
                            .setX(toX(x))
                            .setY(toY(y))
                            .setZIndex(0);
                }
            }
        }
    }

    private void drawEntities() {
        Coord goal = game.getExit();
        goalSprite = graphicEntityModule.createSprite()
                .setImage(Constants.GOAL_SPRITE)
                .setScale(0.5)
                .setX(toX(goal.getX()))
                .setY(toY(goal.getY()))
                .setZIndex(1);

        Coord player = game.getPlayerPos();
        playerSprite = graphicEntityModule.createSprite()
                .setImage(Constants.PLAYER_SPRITE)
                .setScale(0.5)
                .setX(toX(player.getX()))
                .setY(toY(player.getY()))
                .setZIndex(2);
    }

    /**
     * Initializes the game and sends initial input to the player.
     * <p>
     * Sends: WIDTH HEIGHT, EXIT_X EXIT_Y, then HEIGHT rows of grid data (0=wall, 1=floor).
     */
    @Override
    public void init() {
        gameManager.setFrameDuration(300);

        GridDefinition gridDefinition = new BinarySpacePartitioning().generate();
        game = new DungeonGame(gridDefinition);

        drawGrid(gridDefinition.getGrid());
        drawEntities();

        gameManager.getPlayer().sendInputLine(
                Constants.COLUMNS + " " + Constants.ROWS
        );
        gameManager.getPlayer().sendInputLine(game.getExit().toString());
        
        // Send grid as rows of 0s and 1s
        int[][] grid = gridDefinition.getGrid();
        for (int y = 0; y < Constants.ROWS; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < Constants.COLUMNS; x++) {
                row.append(grid[y][x]);
            }
            gameManager.getPlayer().sendInputLine(row.toString());
        }
    }

    /**
     * Executes a single game turn.
     * <p>
     * Sends: PLAYER_X PLAYER_Y (current position).
     * Expects: One action (UP, DOWN, LEFT, RIGHT, or STAY).
     */
    @Override
    public void gameTurn(int turn) {
        gameManager.getPlayer().sendInputLine(game.getPlayerPos().toString());
        gameManager.getPlayer().execute();

        try {
            String output = checkOutput(gameManager.getPlayer().getOutputs());
            Action action = Action.valueOf(output);

            game.move(action);
            updateView();

            if (game.hasWon()) {
                gameManager.winGame("Goal reached!");
            }

        } catch (TimeoutException e) {
            gameManager.loseGame("Timeout");
        }
    }

    private void updateView() {
        Coord p = game.getPlayerPos();
        playerSprite
                .setX(toX(p.getX()), Curve.LINEAR)
                .setY(toY(p.getY()), Curve.LINEAR);
    }

    private int toX(int x) {
        return x * Constants.CELL_SIZE;
    }

    private int toY(int y) {
        return y * Constants.CELL_SIZE;
    }

    private String checkOutput(List<String> outputs) {
        if (outputs.size() != 1) {
            gameManager.loseGame("Expected exactly one action");
            return null;
        }

        String out = outputs.get(0).trim().toUpperCase();

        if (!Arrays.asList(Constants.ACTIONS).contains(out)) {
            gameManager.loseGame("Invalid action: " + out);
            return null;
        }

        return out;
    }
}