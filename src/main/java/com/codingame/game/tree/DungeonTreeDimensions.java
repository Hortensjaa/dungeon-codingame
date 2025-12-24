//package com.codingame.game.tree;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public class DungeonTreeDimensions {
//    private final int right;
//    private final int left;
//    private final int top;
//    private final int bottom;
//
//    public int getTreeWidth() {
//        return left + 1 + right;
//    }
//
//    public int getTreeHeight() {
//        return top + 1 + bottom;
//    }
//}
//
//
//final class DungeonTreeEvaluator {
//
//    public static DungeonTreeDimensions evaluateDimensions(DungeonTree tree) {
//        if (tree.isLeaf()) {
//            return new DungeonTreeDimensions(0, 0, 0, 0);
//        }
//
//        DungeonTreeDimensions right =
//                tree.getRightChild() != null ? evaluateDimensions(tree.getRightChild())
//                        : new DungeonTreeDimensions(0,0,0,0);
//
//        DungeonTreeDimensions left =
//                tree.getLeftChild() != null ? evaluateDimensions(tree.getLeftChild())
//                        : new DungeonTreeDimensions(0,0,0,0);
//
//        DungeonTreeDimensions top =
//                tree.getTopChild() != null ? evaluateDimensions(tree.getTopChild())
//                        : new DungeonTreeDimensions(0,0,0,0);
//
//        DungeonTreeDimensions bottom =
//                tree.getBottomChild() != null ? evaluateDimensions(tree.getBottomChild())
//                        : new DungeonTreeDimensions(0,0,0,0);
//
//        int spaceRight = max(
//                right.getRight() + 1,
//                left.getRight() - 1,
//                top.getRight(),
//                bottom.getRight()
//        );
//
//        int spaceLeft = max(
//                right.getLeft() - 1,
//                left.getLeft() + 1,
//                top.getLeft(),
//                bottom.getLeft()
//        );
//
//        int spaceTop = max(
//                right.getTop(),
//                left.getTop(),
//                top.getTop() + 1,
//                bottom.getTop() - 1
//        );
//
//        int spaceBottom = max(
//                right.getBottom(),
//                left.getBottom(),
//                top.getBottom() - 1,
//                bottom.getBottom() + 1
//        );
//
//        return new DungeonTreeDimensions(
//                spaceRight, spaceLeft, spaceTop, spaceBottom
//        );
//    }
//
//    private static int max(int... v) {
//        int m = v[0];
//        for (int x : v) if (x > m) m = x;
//        return m;
//    }
//}
