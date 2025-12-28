package com.codingame.game;

import com.codingame.game.game_objects.Enemy;
import com.codingame.game.game_objects.GamePlayer;
import com.codingame.game.game_objects.Reward;
import com.codingame.game.generator.GridDefinition;
import com.codingame.game.move.Action;
import com.codingame.game.move.Coord;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
public class DungeonGame {

    private final int[][] grid;
    private final GamePlayer player;
    private final Coord exit;
    private final List<Enemy> enemies;
    private final List<Reward> rewards;

    public DungeonGame(GridDefinition def) {
        this.grid = def.getGrid();
        this.player = new GamePlayer(def.getPlayerStart());
        this.exit = def.getExit();
        this.enemies = def.getEnemies().entrySet()
                .stream()
                .map(entry -> new Enemy(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
        this.rewards = def.getRewards().entrySet()
                .stream()
                .map(entry -> new Reward(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    public boolean move(Action action) {
        Coord next = player.move(action);

        if (isOutOfBounds(next) || isWall(next)) {
            player.undoMove();
            return false;
        }
        return true;
    }

    public void update() {
        Coord playerPos = player.getPosition();

        rewards.removeIf(reward -> {
            if (reward.getPosition().equals(playerPos)) {
                reward.applyEffect(player);
                return true;
            }
            return false;
        });

        for (Enemy enemy : enemies) {
            Coord next = enemy.move(enemy.getAction());
            if (isOutOfBounds(next) || !isRoom(next)) {
                enemy.undoMove();
                enemy.setAction(enemy.getAction().opposite());
            }
            if (enemy.getPosition().equals(playerPos)) {
                enemy.attack(player);
            }
        }
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

    private boolean isRoom(Coord c) {
        return grid[c.getY()][c.getX()] == Constants.ROOM;
    }
}
