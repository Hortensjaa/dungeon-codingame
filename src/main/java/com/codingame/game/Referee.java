package com.codingame.game;

import java.util.Arrays;
import java.util.List;

import com.codingame.game.generators.GridDefinition;
import com.codingame.game.generators.RandomGenerator;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.google.inject.Inject;


public class Referee extends AbstractReferee {

    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private DungeonGame game;
    private Sprite playerSprite;
    private Sprite goalSprite;

    private void drawGrid(int[][] grid) {
        for (int y = 0; y < Constants.ROWS; y++) {
            for (int x = 0; x < Constants.COLUMNS; x++) {
                graphicEntityModule.createSprite()
                        .setImage(grid[y][x] == Constants.FLOOR
                                ? Constants.FLOOR_SPRITE
                                : Constants.WALL_SPRITE)
                        .setX(toX(x))
                        .setY(toY(y))
                        .setZIndex(0);
            }
        }
    }

    private void drawEntities() {
        Coord goal = game.getGoal();
        goalSprite = graphicEntityModule.createSprite()
                .setImage(Constants.GOAL_SPRITE)
                .setX(toX(goal.getX()))
                .setY(toY(goal.getY()))
                .setZIndex(1);

        Coord player = game.getPlayerPos();
        playerSprite = graphicEntityModule.createSprite()
                .setImage(Constants.PLAYER_SPRITE)
                .setX(toX(player.getX()))
                .setY(toY(player.getY()))
                .setZIndex(2);
    }

    @Override
    public void init() {
        gameManager.setFrameDuration(300);

        GridDefinition gridDefinition = new RandomGenerator().generate();
        game = new DungeonGame(gridDefinition);

        drawGrid(gridDefinition.getGrid());
        drawEntities();

        gameManager.getPlayer().sendInputLine(
                Constants.COLUMNS + " " + Constants.ROWS
        );
        gameManager.getPlayer().sendInputLine(game.getGoal().toString());
    }

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