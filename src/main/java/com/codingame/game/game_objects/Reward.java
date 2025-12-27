package com.codingame.game.game_objects;

import com.codingame.game.move.Coord;
import com.codingame.gameengine.module.entities.Sprite;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
public class Reward extends GameObject {
    private final Function<GamePlayer, Void> effect;
    @Setter
    private Sprite sprite;

    public Reward(RewardType type, Coord position) {
        super(type.getFrames(), type.getFilePrefix(), position);
        this.effect = type.getEffect();
    }

    public void applyEffect(GamePlayer player) {
        effect.apply(player);
        if (sprite != null) {
            sprite.setVisible(false);
        }
    }
}
