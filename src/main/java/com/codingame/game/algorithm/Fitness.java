package com.codingame.game.algorithm;

import com.codingame.game.Constants;
import com.codingame.game.tree.DungeonTree;
import com.codingame.game.tree.NodeTypes;

import java.util.*;


public final class Fitness {
    // ------------------ quality ------------------
    // more -> better
    static float countNodes(DungeonTree tree) {
        int count = tree.countNodes();  // todo: rethink
        float value = (float) count / Constants.MAX_NODES;
        return 1.0f - Math.abs(value - 0.75f);
    }

    // which part of the dungeon is on the main path from start to exit; should be ~50%
    static float startToExitPath(DungeonTree tree) {
        List<DungeonTree> nodes = new ArrayList<>();
        tree.collectNodes(nodes);

        DungeonTree start = null;
        DungeonTree exit = null;

        for (DungeonTree node : nodes) {
            if (node.getType() instanceof NodeTypes.Start) {
                start = node;
            } else if (node.getType() instanceof NodeTypes.Exit) {
                exit = node;
            }
        }

        if (start == null || exit == null) {
            return 0;
        }

        int distance = tree.getTreeDistance(start, exit);
        float percent = distance / (float)(tree.countNodes());
        return 1.0f - Math.abs(percent - 0.5f);
    }

    // ------------------ controls ------------------
    static float countNodesControl(DungeonTree tree) {
        int count = tree.countNodes();
        return (float) count / Constants.MAX_NODES;
    }

    // should have start and exit
    static float hasStartAndExit(DungeonTree tree) {
        if (tree.hasStartAndExit()) {
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

    // if there are more than 7 children, collisions are unavoidable
    static float checkGrandchildren(DungeonTree tree) {
        return checkGrandchildrenRec(tree) ? 1f : 0f;
    }

    private static boolean checkGrandchildrenRec(DungeonTree tree) {
        if (tree.getGrandchildren().size() > 7) {
            return false;
        }

        for (DungeonTree child : tree.getChildren()) {
            if (!checkGrandchildrenRec(child)) {
                return false;
            }
        }

        return true;
    }

    // ------------------ other ------------------
    static float averageDifficulty(DungeonTree tree) {
        List<DungeonTree> nodes = new ArrayList<>();
        tree.collectNodes(nodes);

        float totalDifficulty = 0;
        for (DungeonTree node : nodes) {
            totalDifficulty += node.getType().getDifficulty();
        }
        return totalDifficulty / nodes.size();
    }

    static float averageReward(DungeonTree tree) {
        List<DungeonTree> nodes = new ArrayList<>();
        tree.collectNodes(nodes);

        float totalReward = 0;
        for (DungeonTree node : nodes) {
            totalReward += node.getType().getReward();
        }
        return totalReward / nodes.size();
    }

    // ------------- API -------------
    private static float min(float... values) {
        float min = Float.MAX_VALUE;
        for (float v : values) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    private static float quality(DungeonTree tree) {
        return (countNodes(tree) + startToExitPath(tree)) / 2;
    }

    private static float control(DungeonTree tree) {
        return min(hasStartAndExit(tree), checkGrandchildren(tree), countNodesControl(tree));
    }

    // todo: find out a good way to compute diversity between two trees
    private static float diversity(DungeonTree tree1, DungeonTree tree2) {
        return 0;
    }

    public static float fitness(DungeonTree tree, boolean quality, boolean control) {
        float q = quality ? quality(tree): 1.0f;
        float c = control ? control(tree): 1.0f;
        return q * c;
    }
}
