package com.codingame.game.game_objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RewardType {
    MONEY("Money", "rewards/money/"),
    POTION("Potion", "rewards/potion/");

    private final String name;
    private final String filePrefix;
}

