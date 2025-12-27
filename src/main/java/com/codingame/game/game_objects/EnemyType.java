package com.codingame.game.game_objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnemyType {
    FIRE("FIRE", "enemies/fire/cut/", 8),
    GOLEM("GOLEM", "enemies/golem/", 8);

    private final String name;
    private final String filePrefix;
    private final int frames;
}
