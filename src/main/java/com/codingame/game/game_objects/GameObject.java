package com.codingame.game.game_objects;

import com.codingame.game.move.Action;
import com.codingame.game.move.Coord;
import lombok.Getter;

@Getter
public abstract class GameObject {
    private final int FRAMES_COUNT;
    private final String FILE_PREFIX;
    private int animationFrame = 0;
    private Coord position;
    private Action lastAction = Action.STAY;

    public GameObject(int framesCount, String prefix, Coord position) {
        this.FRAMES_COUNT = framesCount;
        this.FILE_PREFIX = prefix;
        this.position = position;
    }

    public String getCurrentSprite() {
        animationFrame++;
        animationFrame = animationFrame % FRAMES_COUNT;
        return FILE_PREFIX + lastAction.getName() + "/" + animationFrame + ".png";
    }

    public Coord move(Action action) {
        this.position = this.position.applyAction(action);
        this.lastAction = action;
        return position;
    }

    public void undoMove() {
        this.position = this.position.applyAction(lastAction.opposite());
    }
}
