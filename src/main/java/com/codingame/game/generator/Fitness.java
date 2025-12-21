package com.codingame.game.generator;

import com.codingame.game.Constants;
import com.codingame.game.generator.tree.DungeonTree;


public final class Fitness {
    // ------------------ quality ------------------
    // longer -> better
    static int longestShortestPath(DungeonTree tree) {
        return 1;
    }
    // more -> better
    static int countNodes(DungeonTree tree) {
        return 1;
    }

    // ------------------ controls ------------------
    // shouldn't exceed max width
    static float getDungeonWidth(DungeonTree tree) {
        int w = tree.getTreeWidth();
        if (w > Constants.MAX_TREE_WIDTH) {
            return -1.0f;
        }
        return (Constants.MAX_TREE_WIDTH - w) / (float)Constants.MAX_TREE_WIDTH;
    }
    // shouldn't exceed max height
    static float getDungeonHeight(DungeonTree tree) {
        int h = tree.getTreeHeight();
        if (h > Constants.MAX_TREE_HEIGHT) {
            return -1.0f;
        }
        return (Constants.MAX_TREE_HEIGHT - h) / (float)Constants.MAX_TREE_HEIGHT;
    }
    // should have start and exit
    static float hasStartAndExit(DungeonTree tree) {
        return 0;
    }

    // ------------------ diversity ------------------
    // the difficulty of accessing nodes from start to exit
    static float cumulativeDifficultyOnMainPath(DungeonTree tree) {
        return 1;
    }
    // the difficulty of accessing given node from the root
    static float cumulativeDifficultyOnPath(DungeonTree tree) {
        return 1;
    }

    // ------------- API -------------
    public static float quality(DungeonTree tree) {
        return 1;
    }

    public static float control(DungeonTree tree) {
        return 1;
    }

    public static float diversity(DungeonTree tree1, DungeonTree tree2) {
        return 1;
    }
}
