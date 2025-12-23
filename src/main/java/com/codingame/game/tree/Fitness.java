package com.codingame.game.tree;

import com.codingame.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Fitness {
    // ------------------ quality ------------------
    // more -> better
    static float countNodes(DungeonTree tree) {
        int count = tree.countNodes();
        int maxNodes = Constants.MAX_TREE_WIDTH * Constants.MAX_TREE_HEIGHT;
        return count / (float) maxNodes;
    }

    // ------------------ controls ------------------
    // shouldn't exceed max width
    static float getDungeonWidth(DungeonTree tree) {
        DungeonTreeEvaluation evaluation = tree.evaluate();
        int w = evaluation.getTreeWidth();
        if (w > Constants.MAX_TREE_WIDTH) {
            return 0.0f;
        }
        return w / (float)Constants.MAX_TREE_WIDTH;
    }

    // shouldn't exceed max height
    static float getDungeonHeight(DungeonTree tree) {
        DungeonTreeEvaluation evaluation = tree.evaluate();
        int h = evaluation.getTreeHeight();
        if (h > Constants.MAX_TREE_HEIGHT) {
            return 0.0f;
        }
        return h / (float)Constants.MAX_TREE_HEIGHT;
    }

    // should have start and exit
    static float hasStartAndExit(DungeonTree tree) {
        if (tree.hasStartAndExit()) {
            return 1.0f;
        } else {
            return 0.0f;
        }
    }

    // ------------------ other ------------------
    // the average difficulty of nodes from start to exit
    static float difficultyOnMainPath(DungeonTree tree) {
        List<DungeonTree> nodes = new ArrayList<>();
        tree.collectNodes(nodes);

        DungeonTree start = null;
        DungeonTree exit = null;

        for (DungeonTree node : nodes) {
            if (node.getRoom() instanceof RoomTypes.Start) {
                start = node;
            } else if (node.getRoom() instanceof RoomTypes.Exit) {
                exit = node;
            }
        }

        if (start == null || exit == null) {
            return 0;
        }

        // map {node: difficulty of path from start to node (included)}
        Map<DungeonTree, Float> sumsFromStart = new HashMap<>();
        float cumulativeSumFromStart = 0;
        DungeonTree cur = start;

        while (cur != null) {
            cumulativeSumFromStart += cur.getRoom().getDifficulty();
            sumsFromStart.put(cur, cumulativeSumFromStart);
            cur = cur.getParent();
        }

        cur = exit;
        float cumulativeSumFromExit = 0;

        while (cur != null) {
            if (sumsFromStart.containsKey(cur)) {
                return cumulativeSumFromExit + sumsFromStart.get(cur);
            }
            cumulativeSumFromExit += cur.getRoom().getDifficulty();
            cur = cur.getParent();
        }
        return -1.0f; // should not reach here
    }

    // which part of the dungeon is on the main path from start to exit
    static float startToExitPath(DungeonTree tree) {
        List<DungeonTree> nodes = new ArrayList<>();
        tree.collectNodes(nodes);

        DungeonTree start = null;
        DungeonTree exit = null;

        for (DungeonTree node : nodes) {
            if (node.getRoom() instanceof RoomTypes.Start) {
                start = node;
            } else if (node.getRoom() instanceof RoomTypes.Exit) {
                exit = node;
            }
        }

        if (start == null || exit == null) {
            return 0;
        }

        int distance = tree.getTreeDistance(start, exit);
        return distance / (float)(tree.countNodes());
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
        return countNodes(tree);
    }

    private static float control(DungeonTree tree) {
        return min(getDungeonWidth(tree), getDungeonHeight(tree), hasStartAndExit(tree));
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
