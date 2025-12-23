package com.codingame.game.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DungeonTreeEvaluation {
    private final int right;
    private final int left;
    private final int top;
    private final int bottom;

    public int getTreeWidth() {
        return left + 1 + right;
    }

    public int getTreeHeight() {
        return top + 1 + bottom;
    }
}


final class DungeonTreeEvaluator {

    public static DungeonTreeEvaluation evaluate(DungeonTree tree) {
        if (tree.isLeaf()) {
            return new DungeonTreeEvaluation(0, 0, 0, 0);
        }

        DungeonTreeEvaluation right =
                tree.getRightChild() != null ? evaluate(tree.getRightChild())
                        : new DungeonTreeEvaluation(0,0,0,0);

        DungeonTreeEvaluation left =
                tree.getLeftChild() != null ? evaluate(tree.getLeftChild())
                        : new DungeonTreeEvaluation(0,0,0,0);

        DungeonTreeEvaluation top =
                tree.getTopChild() != null ? evaluate(tree.getTopChild())
                        : new DungeonTreeEvaluation(0,0,0,0);

        DungeonTreeEvaluation bottom =
                tree.getBottomChild() != null ? evaluate(tree.getBottomChild())
                        : new DungeonTreeEvaluation(0,0,0,0);

        int spaceRight = max(
                right.getRight() + 1,
                left.getRight() - 1,
                top.getRight(),
                bottom.getRight()
        );

        int spaceLeft = max(
                right.getLeft() - 1,
                left.getLeft() + 1,
                top.getLeft(),
                bottom.getLeft()
        );

        int spaceTop = max(
                right.getTop(),
                left.getTop(),
                top.getTop() + 1,
                bottom.getTop() - 1
        );

        int spaceBottom = max(
                right.getBottom(),
                left.getBottom(),
                top.getBottom() - 1,
                bottom.getBottom() + 1
        );

        return new DungeonTreeEvaluation(
                spaceRight, spaceLeft, spaceTop, spaceBottom
        );
    }

    private static int max(int... v) {
        int m = v[0];
        for (int x : v) if (x > m) m = x;
        return m;
    }
}
