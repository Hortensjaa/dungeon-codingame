package com.codingame.game.generators.tree;

/**
 * Public API to build and configure DungeonTree.
 *
 * Usage:
 * <pre>
 * DungeonTree tree = DungeonTreeBuilder.create()
 *         .maxDepth(5)
 *         .branchingFactorX(0.9f)
 *         .branchingFactorY(0.7f)
 *         .branchingFactorMultiplier(0.9f)
 *         .build();
 * </pre>
 */
public class DungeonTreeBuilder {

    private int maxDepth = 5;
    private float branchingFactorX = 0.9f;
    private float branchingFactorY = 0.7f;
    private float branchingFactorMultiplier = 0.9f;

    private DungeonTreeBuilder() {
    }

    /**
     * Creates a new builder instance.
     */
    public static DungeonTreeBuilder create() {
        return new DungeonTreeBuilder();
    }

    /**
     * Sets the maximum depth of the tree.
     * @param maxDepth maximum depth (default: 5)
     */
    public DungeonTreeBuilder maxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    /**
     * Sets the branching factors - probability of creating a child nodes.
     * @param branchingFactor value between 0.0 and 1.0
     */
    public DungeonTreeBuilder branchingFactorX(float branchingFactor) {
        this.branchingFactorX = branchingFactor;
        return this;
    }

    public DungeonTreeBuilder branchingFactorY(float branchingFactor) {
        this.branchingFactorY = branchingFactor;
        return this;
    }

    /**
     * Sets the multiplier applied to branching factor at each depth level.
     * @param branchingFactorMultiplier value between 0.0 and 1.0 (default: 0.9)
     */
    public DungeonTreeBuilder branchingFactorMultiplier(float branchingFactorMultiplier) {
        this.branchingFactorMultiplier = branchingFactorMultiplier;
        return this;
    }

    /**
     * Builds the dungeon tree with the configured parameters.
     * The tree is generated, evaluated for space, and returned ready to use.
     *
     * @return the root of the generated dungeon tree
     */
    public DungeonTree build() {
        DungeonTree tree = new DungeonTree();
        tree.generateRandomTree(maxDepth, branchingFactorX, branchingFactorY, branchingFactorMultiplier);
        tree.evaluateSpaceNeeded();
        return tree;
    }
}

