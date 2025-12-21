package com.codingame.game.generators.tree;

import com.codingame.game.move.Direction;
import lombok.Data;

@Data
public class DungeonTree {

    // data
    private RoomTypes.Base room;
    //children
    private DungeonTree rightChild = null;
    private DungeonTree leftChild = null;
    private DungeonTree topChild = null;
    private DungeonTree bottomChild = null;
    // parent
    private Direction parentDirection = null; // if parent direction is top, there is no topChild etc.
    private int depth;
    // evaluation of space needed by every node
    private int spaceRight = -1;
    private int spaceLeft = -1;
    private int spaceTop = -1;
    private int spaceBottom = -1;

    // --------------- building tree ---------------
    public void generateRandomTree(
            int maxDepth,
            float branchingFactorX,
            float branchingFactorY,
            float branchingFactorMultiplier
    ) {
        generateSubtree(0, maxDepth, branchingFactorX, branchingFactorY, branchingFactorMultiplier, Direction.UP, true);

        // Find the two most distant leaves for Start and Exit
        DungeonTree[] mostDistantLeaves = findMostDistantLeaves();
        if (mostDistantLeaves[0] != null) {
            mostDistantLeaves[0].room = new RoomTypes.Start();
        }
        if (mostDistantLeaves[1] != null) {
            mostDistantLeaves[1].room = new RoomTypes.Exit();
        }
    }

    private boolean isLeaf() {
        return leftChild == null && rightChild == null && bottomChild == null && topChild == null;
    }

    private void generateSubtree(
            int currentDepth,
            int maxDepth,
            float branchingFactorX,
            float branchingFactorY,
            float branchingFactorMultiplier,
            Direction parentDirection
    ) {
        generateSubtree(
                currentDepth,
                maxDepth,
                branchingFactorX,
                branchingFactorY,
                branchingFactorMultiplier,
                parentDirection,
                false
        );
    }

    private void generateSubtree(
            int currentDepth,
            int maxDepth,
            float branchingFactorX,
            float branchingFactorY,
            float branchingFactorMultiplier,
            Direction parentDirection,
            boolean isRoot
    ) {
        this.depth = currentDepth;
        if (!isRoot) {
            this.parentDirection = parentDirection;
        }
        room = RoomTypes.getRandomRoom();

        if (currentDepth >= maxDepth) {
            return;
        }

        leftChild = (Math.random() < branchingFactorX && parentDirection != Direction.LEFT) ? new DungeonTree() : null;
        rightChild = (Math.random() < branchingFactorX && parentDirection != Direction.RIGHT) ? new DungeonTree() : null;
        bottomChild = (Math.random() < branchingFactorY && parentDirection != Direction.DOWN) ? new DungeonTree() : null;
        topChild = (Math.random() < branchingFactorY && parentDirection != Direction.UP) ? new DungeonTree() : null;

        if (leftChild != null) leftChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactorX * branchingFactorMultiplier,
                branchingFactorY,
                branchingFactorMultiplier,
                Direction.RIGHT
        );
        if (rightChild != null) rightChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactorX * branchingFactorMultiplier,
                branchingFactorY,
                branchingFactorMultiplier,
                Direction.LEFT
        );
        if (bottomChild != null) bottomChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactorX,
                branchingFactorY * branchingFactorMultiplier,
                branchingFactorMultiplier,
                Direction.UP
        );
        if (topChild != null) topChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactorX,
                branchingFactorY * branchingFactorMultiplier,
                branchingFactorMultiplier,
                Direction.DOWN
        );
    }

    private DungeonTree[] findMostDistantLeaves() {
        java.util.List<DungeonTree> leaves = new java.util.ArrayList<>();
        collectLeaves(leaves);

        if (leaves.size() < 2) {
            // If only one leaf or none, return it as both start and exit
            DungeonTree leaf = leaves.isEmpty() ? this : leaves.get(0);
            return new DungeonTree[]{leaf, leaf};
        }

        DungeonTree leaf1 = null;
        DungeonTree leaf2 = null;
        int maxDistance = -1;

        for (int i = 0; i < leaves.size(); i++) {
            for (int j = i + 1; j < leaves.size(); j++) {
                int distance = getTreeDistance(leaves.get(i), leaves.get(j));
                if (distance > maxDistance) {
                    maxDistance = distance;
                    leaf1 = leaves.get(i);
                    leaf2 = leaves.get(j);
                }
            }
        }

        return new DungeonTree[]{leaf1, leaf2};
    }

    private void collectLeaves(java.util.List<DungeonTree> leaves) {
        if (isLeaf()) {
            leaves.add(this);
            return;
        }
        if (leftChild != null) leftChild.collectLeaves(leaves);
        if (rightChild != null) rightChild.collectLeaves(leaves);
        if (topChild != null) topChild.collectLeaves(leaves);
        if (bottomChild != null) bottomChild.collectLeaves(leaves);
    }

    private int getTreeDistance(DungeonTree a, DungeonTree b) {
        // Get paths from root to both nodes
        java.util.List<DungeonTree> pathA = new java.util.ArrayList<>();
        java.util.List<DungeonTree> pathB = new java.util.ArrayList<>();
        findPath(this, a, pathA);
        findPath(this, b, pathB);

        // Find lowest common ancestor
        int lcaIndex = 0;
        while (lcaIndex < pathA.size() && lcaIndex < pathB.size()
                && pathA.get(lcaIndex) == pathB.get(lcaIndex)) {
            lcaIndex++;
        }

        // Distance = steps from a to LCA + steps from LCA to b
        return (pathA.size() - lcaIndex) + (pathB.size() - lcaIndex);
    }

    private boolean findPath(DungeonTree current, DungeonTree target, java.util.List<DungeonTree> path) {
        if (current == null) return false;

        path.add(current);

        if (current == target) return true;

        if (findPath(current.leftChild, target, path)) return true;
        if (findPath(current.rightChild, target, path)) return true;
        if (findPath(current.topChild, target, path)) return true;
        if (findPath(current.bottomChild, target, path)) return true;

        path.remove(path.size() - 1);
        return false;
    }

    // --------------- space needed evaluation ---------------
    public void evaluateSpaceNeeded() {
        if (isLeaf()) {
            spaceRight = 0;
            spaceLeft = 0;
            spaceTop = 0;
            spaceBottom = 0;
            return;
        }

        if (rightChild != null) {
            rightChild.evaluateSpaceNeeded();
        }
        if (leftChild != null) {
            leftChild.evaluateSpaceNeeded();
        }
        if (topChild != null) {
            topChild.evaluateSpaceNeeded();
        }
        if (bottomChild != null) {
            bottomChild.evaluateSpaceNeeded();
        }

        spaceRight = max(
                getChildSpaceNeeded(rightChild, Direction.RIGHT) + 1,
                getChildSpaceNeeded(leftChild, Direction.RIGHT) - 1,
                getChildSpaceNeeded(topChild, Direction.RIGHT),
                getChildSpaceNeeded(bottomChild, Direction.RIGHT)
        );
        spaceLeft = max(
                getChildSpaceNeeded(rightChild, Direction.LEFT) - 1,
                getChildSpaceNeeded(leftChild, Direction.LEFT) + 1,
                getChildSpaceNeeded(topChild, Direction.LEFT),
                getChildSpaceNeeded(bottomChild, Direction.LEFT)
        );
        spaceTop = max(
                getChildSpaceNeeded(rightChild, Direction.UP),
                getChildSpaceNeeded(leftChild, Direction.UP),
                getChildSpaceNeeded(topChild, Direction.UP) + 1,
                getChildSpaceNeeded(bottomChild, Direction.UP) - 1
        );
        spaceBottom = max(
                getChildSpaceNeeded(rightChild, Direction.UP),
                getChildSpaceNeeded(leftChild, Direction.UP),
                getChildSpaceNeeded(topChild, Direction.UP) - 1,
                getChildSpaceNeeded(bottomChild, Direction.UP) + 1
        );
    }

    private int getChildSpaceNeeded(DungeonTree child, Direction direction) {
        if (child == null) return 0;
        switch (direction) {
            case UP:
                return child.spaceTop;
            case DOWN:
                return child.spaceBottom;
            case LEFT:
                return child.spaceLeft;
            case RIGHT:
                return child.spaceRight;
            default:
                return 0;
        }
    }

    private int max(int... values) {
        int max = values[0];
        for (int v : values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    // --------------- testing ---------------
    public void print(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("-");
        }
        System.out.println(sb.toString() + room.name);
        if (topChild != null) {
            System.out.println(sb.toString() + "top: ");
            topChild.print(depth + 1);
        }
        if (rightChild != null) {
            System.out.println(sb.toString() + "right: ");
            rightChild.print(depth + 1);
        }
        if (leftChild != null) {
            System.out.println(sb.toString() + "left: ");
            leftChild.print(depth + 1);
        }
        if (bottomChild != null) {
            System.out.println(sb.toString() + "bottom: ");
            bottomChild.print(depth + 1);
        }
    }

    public int getTreeWidth() {
        return spaceLeft + 1 + spaceRight;
    }

    public int getTreeHeight() {
        return spaceTop + 1 + spaceBottom;
    }
}
