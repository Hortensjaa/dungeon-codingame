package com.codingame.game.generator;

import com.codingame.game.move.Coord;
import com.codingame.game.move.Direction;
import com.codingame.game.tree.NodeTypes;

import java.util.HashSet;


public class LayoutField {
    NodeTypes.Base type;
    Direction parentDirection;

    HashSet<Coord> placedChildrenPositions = new HashSet<>(); // to backtrack - recursively remove children placements

    public LayoutField(NodeTypes.Base type, Direction parentDirection) {
        this.type = type;
        this.parentDirection = parentDirection;
    }

    public String toString() {
        return "[" + type.getShortName() + "] ";
    }
}
