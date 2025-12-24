package com.codingame.game.algorithm;

import com.codingame.game.move.Direction;
import com.codingame.game.tree.DungeonTree;
import com.codingame.game.tree.NodeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class MapElites {
    final static int GENERATIONS_DEFAULT = 10_000_000;
    final static int BASE_POPULATION = 100;

    private static DungeonTree getRandomNode(DungeonTree root) {
        List<DungeonTree> nodes = new ArrayList<>();
        root.collectNodes(nodes);
        return nodes.get(ThreadLocalRandom.current().nextInt(nodes.size()));
    }

// ------------------------- mutations -------------------------
    private static void changeRoomType(DungeonTree tree) {
        tree.setType(NodeTypes.getRandomRoom());
    }

    private static void swapRoomTypes(DungeonTree tree1, DungeonTree tree2) {
        NodeTypes.Base temp = tree1.getType();
        tree1.setType(tree2.getType());
        tree2.setType(temp);
    }

    private static void swapSubtrees(DungeonTree tree) {
        if (tree.getParentDirection() == Direction.LEFT || tree.getParentDirection() == Direction.RIGHT) {
            DungeonTree top = tree.getTopChild();
            DungeonTree bottom = tree.getBottomChild();
            tree.setTopChild(bottom);
            tree.setBottomChild(top);
        } else {
            DungeonTree left = tree.getLeftChild();
            DungeonTree right = tree.getRightChild();
            tree.setLeftChild(right);
            tree.setRightChild(left);
        }
    }

    private static void addChildNode(DungeonTree tree) {
        float rand = (float)Math.random();
        if (rand < 0.25f && tree.getTopChild() == null && tree.getParentDirection() != Direction.DOWN) {
            tree.setTopChild(new DungeonTree(Direction.DOWN));
        } else if (rand < 0.5f && tree.getBottomChild() == null && tree.getParentDirection() != Direction.UP) {
            tree.setBottomChild(new DungeonTree(Direction.UP));
        } else if (rand < 0.75f && tree.getLeftChild() == null && tree.getParentDirection() != Direction.RIGHT) {
            tree.setLeftChild(new DungeonTree(Direction.RIGHT));
        } else if (tree.getRightChild() == null && tree.getParentDirection() != Direction.LEFT) {
            tree.setRightChild(new DungeonTree(Direction.LEFT));
        }
    }

    private static void removeChildNode(DungeonTree tree) {
        float rand = (float)Math.random();
        DungeonTree removed = null;
        if (rand < 0.25f && tree.getTopChild() != null) {
            removed = tree.getTopChild();
            tree.setTopChild(null);
        } else if (rand < 0.5f && tree.getBottomChild() != null) {
            removed = tree.getBottomChild();
            tree.setBottomChild(null);
        } else if (rand < 0.75f && tree.getLeftChild() != null) {
            removed = tree.getLeftChild();
            tree.setLeftChild(null);
        } else if (tree.getRightChild() != null) {
            removed = tree.getRightChild();
            tree.setRightChild(null);
        }
        if (removed != null && removed.getType() instanceof NodeTypes.Start) {
            tree.setType(new NodeTypes.Start());
        }
        if (removed != null && removed.getType() instanceof NodeTypes.Exit) {
            tree.setType(new NodeTypes.Exit());
        }
    }

    private static DungeonTree mutate(DungeonTree tree) {
        DungeonTree treeCopy = tree.deepCopy();
        DungeonTree randomChild = getRandomNode(treeCopy);
        if (randomChild == null) return treeCopy;
        float rand = (float)Math.random();
        if (randomChild.isLeaf()) {
            if (rand < 0.5f) {
                changeRoomType(randomChild);
            } else {
                addChildNode(randomChild);
            }
        } else if (randomChild.isStartOrExit()) {
             if (rand < 0.25f) {
                swapSubtrees(randomChild);
            } else if (rand < 0.5f) {
                 DungeonTree randomChild2 = getRandomNode(treeCopy);
                 swapRoomTypes(randomChild, randomChild2);
            } else if (rand < 0.75f) {
                addChildNode(randomChild);
            } else {
                removeChildNode(randomChild);
            }
        } else {
            if (rand < 0.25f) {
                changeRoomType(randomChild);
            } else if (rand < 0.5f) {
                swapSubtrees(randomChild);
            } else if (rand < 0.75f) {
                addChildNode(randomChild);
            } else {
                removeChildNode(randomChild);
            }
        }
        return treeCopy;
    }

// ------------------------- crossover -------------------------
    private static DungeonTree crossover(DungeonTree parent1, DungeonTree parent2) {
        DungeonTree child = parent1.deepCopy();
        if (Math.random() < 0.5f && parent2.getLeftChild() != null) {
            child.setLeftChild(parent2.getLeftChild().deepCopy());
        }
        if (Math.random() < 0.5f && parent2.getRightChild() != null) {
            child.setRightChild(parent2.getRightChild().deepCopy());
        }
        if (Math.random() < 0.5f && parent2.getTopChild() != null) {
            child.setTopChild(parent2.getTopChild().deepCopy());
        }
        if (Math.random() < 0.5f && parent2.getBottomChild() != null) {
            child.setBottomChild(parent2.getBottomChild().deepCopy());
        }
        return child;
    }

// ------------------------- algorithm -------------------------
    public static MapElitesArchive run() {
        return MapElites.run(GENERATIONS_DEFAULT);
    }

    public static MapElitesArchive run(int generations_num) {
        // -- initialize
        MapElitesArchive archive = new MapElitesArchive(
                Fitness::averageDifficulty,
                Fitness::averageReward,
                tree -> Fitness.fitness(tree, true, true),
                0.1f, 0.7f,
                0.2f, 0.8f
        );
        archive.populateArchive(BASE_POPULATION);
        // -- main loop
        for (int generation = 0; generation < generations_num; generation++) {
            float rand = (float)Math.random();
            if (rand < 0.4f) {
                // mutation
                DungeonTree parent = archive.getRandomTree();
                if (parent != null) {
                    DungeonTree child = mutate(parent);
                    archive.addToArchive(child);
                }
            } else {
                // crossover
                DungeonTree parent1 = archive.getRandomTree();
                DungeonTree parent2 = archive.getRandomTree();
                if (parent1 != null && parent2 != null) {
                    DungeonTree child = crossover(parent1, parent2);
                    archive.addToArchive(child);
                }
            }
        }
        return archive;
    }

    public static void main(String[] args) {
        int numGenerations = 10000;
        MapElitesArchive res = MapElites.run(numGenerations);
        res.print();
        res.serializeArchive(numGenerations);
    }
}
