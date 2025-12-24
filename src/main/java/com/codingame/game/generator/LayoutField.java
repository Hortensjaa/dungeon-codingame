package com.codingame.game.generator;

import com.codingame.game.move.Direction;
import com.codingame.game.tree.NodeTypes;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class LayoutField {
    NodeTypes.Base type;
    Direction parentDirection;

    public String toString() {
        return "[" + type.getShortName() + "] ";
    }
}
