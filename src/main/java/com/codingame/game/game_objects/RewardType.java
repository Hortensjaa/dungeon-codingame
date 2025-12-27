package com.codingame.game.game_objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum RewardType {
    COIN("COIN", "rewards/coin/", 1, GamePlayer::addScore),
    HEALTH_POTION("HEALTH POTION", "rewards/potions/health/", 8, GamePlayer::addLife);

    private final String name;
    private final String filePrefix;
    private final int frames;
    private final Function<GamePlayer, Void> effect;

    private static final float POTION_PROBABILITY = 0.1f;

    public static RewardType getRandom() {
        return Math.random() < POTION_PROBABILITY ? HEALTH_POTION : COIN;
    }
}

