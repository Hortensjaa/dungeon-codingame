package com.codingame.game.generators;

import com.codingame.game.Constants;
import com.codingame.game.generators.tree.DungeonTree;


public final class Fitness {
    static int longestShortestPath(DungeonTree tree) {
        return 1;
    } // more -> better (quality)
    static int countNodes(DungeonTree tree) {
        return 1;
    } // more -> better (quality)
    static float enemiesDifficultySum(DungeonTree tree) {
        return 1;
    }
    static float rewardsSum(DungeonTree tree) {
        return 1;
    }
    static float enemiesSumOnMainPath(DungeonTree tree) {
        return 1;
    }
    // the difficulty of accessing given node from the root
    static float cumulativeDifficultyOnPath(DungeonTree tree) {
        return 1;
    }

    // shouldn't exceed max width (control)
    static float getDungeonWidth(DungeonTree tree) {
        int w = tree.getTreeWidth();
        if (w > Constants.MAX_TREE_WIDTH) {
            return -1.0f;
        }
        return (Constants.MAX_TREE_WIDTH - w) / (float)Constants.MAX_TREE_WIDTH;
    }

    // shouldn't exceed max height (control)
    static float getDungeonHeight(DungeonTree tree) {
        int h = tree.getTreeHeight();
        if (h > Constants.MAX_TREE_HEIGHT) {
            return -1.0f;
        }
        return (Constants.MAX_TREE_HEIGHT - h) / (float)Constants.MAX_TREE_HEIGHT;
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
