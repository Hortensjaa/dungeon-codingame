package com.codingame.game.tree;

import com.codingame.game.move.Direction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@NoArgsConstructor
public class DungeonTree {

    // data
    @Setter
    private NodeTypes.Base room;
    //children
    private DungeonTree rightChild = null;
    private DungeonTree leftChild = null;
    private DungeonTree topChild = null;
    private DungeonTree bottomChild = null;
    // parent
    private DungeonTree parent = null;
    private Direction parentDirection; // if parent direction is top, there is no topChild etc.
    private int depth;

    // --------------- constructors ---------------
    public DungeonTree(NodeTypes.Base room, Direction parentDirection) {
        this.room = room;
        this.parentDirection = parentDirection;
    }

    public DungeonTree(Direction parentDirection) {
        this.room = NodeTypes.getRandomRoom();
        this.parentDirection = parentDirection;
    }

    public DungeonTree(Direction parentDirection, DungeonTree parent) {
        this.room = NodeTypes.getRandomRoom();
        this.parentDirection = parentDirection;
        this.parent = parent;
    }

    // --------------- building tree ---------------
    public void generateRandomTree(
            int maxDepth,
            float branchingFactor,
            float branchingFactorMultiplier
    ) {
        generateSubtree(0, maxDepth, branchingFactor, branchingFactorMultiplier);

        // Find the two most distant leaves for Start and Exit
        DungeonTree[] mostDistantLeaves = findMostDistantLeaves();
        if (mostDistantLeaves[0] != null) {
            mostDistantLeaves[0].room = new NodeTypes.Start();
        }
        if (mostDistantLeaves[1] != null) {
            mostDistantLeaves[1].room = new NodeTypes.Exit();
        }
    }

    boolean isLeaf() {
        return leftChild == null && rightChild == null && bottomChild == null && topChild == null;
    }

    private void generateSubtree(
            int currentDepth,
            int maxDepth,
            float branchingFactor,
            float branchingFactorMultiplier
    ) {
        this.depth = currentDepth;
        room = NodeTypes.getRandomRoom();

        if (currentDepth >= maxDepth) {
            return;
        }

        leftChild = (Math.random() < branchingFactor && parentDirection != Direction.LEFT) ?
                new DungeonTree(Direction.RIGHT, this) : null;
        rightChild = (Math.random() < branchingFactor && parentDirection != Direction.RIGHT) ?
                new DungeonTree(Direction.LEFT, this) : null;
        bottomChild = (Math.random() < branchingFactor && parentDirection != Direction.DOWN) ?
                new DungeonTree(Direction.UP, this) : null;
        topChild = (Math.random() < branchingFactor && parentDirection != Direction.UP) ?
                new DungeonTree(Direction.DOWN, this) : null;

        if (leftChild != null) leftChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactor * branchingFactorMultiplier,
                branchingFactorMultiplier
        );
        if (rightChild != null) rightChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactor * branchingFactorMultiplier,
                branchingFactorMultiplier
        );
        if (bottomChild != null) bottomChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactor * branchingFactorMultiplier,
                branchingFactorMultiplier
        );
        if (topChild != null) topChild.generateSubtree(
                currentDepth + 1,
                maxDepth,
                branchingFactor * branchingFactorMultiplier,
                branchingFactorMultiplier
        );
    }

    private DungeonTree[] findMostDistantLeaves() {
        List<DungeonTree> leaves = new ArrayList<>();
        collectLeaves(leaves);

        if (leaves.size() < 2) {
            // If only one leaf or none, return it as both start as root
            DungeonTree leaf = leaves.isEmpty() ? this : leaves.get(0);
            return new DungeonTree[]{this, leaf};
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

    public void collectLeaves(List<DungeonTree> leaves) {
        if (isLeaf()) {
            leaves.add(this);
            return;
        }
        if (leftChild != null) leftChild.collectLeaves(leaves);
        if (rightChild != null) rightChild.collectLeaves(leaves);
        if (topChild != null) topChild.collectLeaves(leaves);
        if (bottomChild != null) bottomChild.collectLeaves(leaves);
    }

    int getTreeDistance(DungeonTree a, DungeonTree b) {
        if (a == b) return 0;

        Map<DungeonTree, Integer> distFromA = new HashMap<>();
        int dist = 0;
        DungeonTree cur = a;

        while (cur != null) {
            distFromA.put(cur, dist++);
            cur = cur.getParent();
        }

        cur = b;
        dist = 0;

        while (cur != null) {
            if (distFromA.containsKey(cur)) {
                return dist + distFromA.get(cur);
            }
            dist++;
            cur = cur.getParent();
        }

        return Integer.MAX_VALUE;
    }


    private boolean findPath(DungeonTree current, DungeonTree target, List<DungeonTree> path) {
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

    // --------------- setters ---------------
    public void setTopChild(DungeonTree newChild) {
        this.topChild = newChild;
        if (topChild != null) {
            topChild.parentDirection = Direction.DOWN;
            topChild.parent = this;
        }
    }

    public void setLeftChild(DungeonTree newChild) {
        this.leftChild = newChild;
        if (leftChild != null) {
            leftChild.parentDirection = Direction.RIGHT;
            leftChild.parent = this;
        }
    }

    public void setRightChild(DungeonTree newChild) {
        this.rightChild = newChild;
        if (rightChild != null) {
            rightChild.parentDirection = Direction.LEFT;
            rightChild.parent = this;
        }
    }

    public void setBottomChild(DungeonTree newChild) {
        this.bottomChild = newChild;
        if (bottomChild != null) {
            bottomChild.parentDirection = Direction.UP;
            bottomChild.parent = this;
        }
    }

    // --------------- testing ---------------
    private void print(int depth) {
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

    public void print() {
        print(0);
    }

    private DungeonTree deepCopy(DungeonTree parent) {
        DungeonTree copy = new DungeonTree(this.room, this.parentDirection);
        copy.parent = parent;
        if (leftChild != null) {
            copy.leftChild = leftChild.deepCopy(copy);
        }
        if (rightChild != null) {
            copy.rightChild = rightChild.deepCopy(copy);
        }
        if (topChild != null) {
            copy.topChild = topChild.deepCopy(copy);
        }
        if (bottomChild != null) {
            copy.bottomChild = bottomChild.deepCopy(copy);
        }
        return copy;
    }

    public DungeonTree deepCopy() {
        return deepCopy(null);
    }

    public void collectNodes(List<DungeonTree> out) {
        out.add(this);

        if (leftChild != null) leftChild.collectNodes(out);
        if (rightChild != null) rightChild.collectNodes(out);
        if (topChild != null) topChild.collectNodes(out);
        if (bottomChild != null) bottomChild.collectNodes(out);
    }

    boolean isStartOrExit() {
        return room instanceof NodeTypes.Start || room instanceof NodeTypes.Exit;
    }

    public int countNodes() {
        List<DungeonTree> nodes = new ArrayList<>();
        collectNodes(nodes);
        return nodes.size();
    }

    public boolean hasStartAndExit() {
        List<DungeonTree> nodes = new ArrayList<>();
        collectNodes(nodes);
        boolean hasStart = false;
        boolean hasExit = false;
        for (DungeonTree node : nodes) {
            if (node.room instanceof NodeTypes.Start) {
                hasStart = true;
            }
            if (node.room instanceof NodeTypes.Exit) {
                hasExit = true;
            }
        }
        return hasStart && hasExit;
    }

    // --------------- evaluation ---------------
    public DungeonTreeDimensions evaluate() {
        return DungeonTreeEvaluator.evaluateDimensions(this);
    }

}
