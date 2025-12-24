package com.codingame.game.algorithm;

import com.codingame.game.Constants;
import com.codingame.game.move.Coord;
import com.codingame.game.tree.DungeonTree;
import com.codingame.game.tree.DungeonTreeDimensions;
import com.codingame.game.tree.NodeTypes;

import java.util.*;


public final class Fitness {
    // ------------------ quality ------------------
    // more -> better
    static float countNodes(DungeonTree tree) {
        int count = tree.countNodes();
        int maxNodes = Constants.MAX_TREE_WIDTH * Constants.MAX_TREE_HEIGHT * 2; // todo: rethink colliding nodes
        if (count > maxNodes) {
            return 0.0f;
        }
        return count / (float) maxNodes;
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

    // todo: main path interesting(ness?) - curves preferred over straight line

    // ------------------ controls ------------------
    // shouldn't exceed max width
    static float getDungeonWidth(DungeonTree tree) {
        DungeonTreeDimensions evaluation = tree.evaluate();
        int w = evaluation.getTreeWidth();
        if (w > Constants.MAX_TREE_WIDTH) {
            return 0.0f;
        }
        return w / (float)Constants.MAX_TREE_WIDTH;
    }

    // shouldn't exceed max height
    static float getDungeonHeight(DungeonTree tree) {
        DungeonTreeDimensions evaluation = tree.evaluate();
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

    private static boolean collisionsHelper(
            DungeonTree tree,
            Set<Coord> visited,
            Coord pos
    ) {
        if (tree == null) {
            return true;
        }

        if (!visited.add(pos)) {
            return false;
        }

        return collisionsHelper(tree.getRightChild(), visited, pos.add(new Coord( 1,  0))) &&
                collisionsHelper(tree.getLeftChild(), visited, pos.add(new Coord(-1,  0))) &&
                collisionsHelper(tree.getTopChild(), visited, pos.add(new Coord( 0,  1))) &&
                collisionsHelper(tree.getBottomChild(), visited, pos.add(new Coord( 0, -1)));
    }


    // doesn't allow collisions
    static float checkCollisions(DungeonTree tree) {
        Set<Coord> visited = new HashSet<>();
        boolean ok = collisionsHelper(tree, visited, new Coord(0, 0));
        return ok ? 1f : 0f;
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
        return countNodes(tree) + startToExitPath(tree);
    }

    private static float control(DungeonTree tree) {
        return min(getDungeonWidth(tree), getDungeonHeight(tree), hasStartAndExit(tree), checkCollisions(tree));
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
