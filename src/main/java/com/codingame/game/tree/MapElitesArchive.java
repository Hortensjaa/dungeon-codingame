package com.codingame.game.tree;

import com.codingame.game.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

@Data
@AllArgsConstructor
class ArchiveRecord {
    DungeonTree tree;
    float fitness;
}

public class MapElitesArchive {
    private final int SIZE = 10;
    private final Function<DungeonTree, Float> xAxisFunction;
    private final Function<DungeonTree, Float> yAxisFunction;
    private final Function<DungeonTree, Float> fitnessFunction;

    private final float min_x;
    private final float max_x;
    private final float min_y;
    private final float max_y;
    private final ArchiveRecord[][] archive;

    public MapElitesArchive(
            Function<DungeonTree, Float> xAxisFunction,
            Function<DungeonTree, Float> yAxisFunction,
            Function<DungeonTree, Float> fitnessFunction,
            float min_x, float max_x, float min_y, float max_y
    ) {
        archive = new ArchiveRecord[SIZE][SIZE];
        this.xAxisFunction = xAxisFunction;
        this.yAxisFunction = yAxisFunction;
        this.fitnessFunction = fitnessFunction;
        this.max_x = max_x;
        this.min_x = min_x;
        this.max_y = max_y;
        this.min_y = min_y;
    }

    public void populateArchive(int numIndividuals) {
        for (int i = 0; i < numIndividuals; i++) {
            DungeonTree tree = new DungeonTree();
            tree.generateRandomTree(
                    (int) (Constants.MIN_DEPTH + Math.random() * (Constants.MAX_DEPTH - Constants.MIN_DEPTH)),
                    (float) (0.25 + Math.random() * 0.5),
                    (float) (0.5 + Math.random() * 0.5)
            );
            addToArchive(tree);
        }
    }

    private int mapToIndex(float value, float min, float max) {
        if (value < min || value > max) {
            return -1; // outside archive bounds
        }
        float normalized = (value - min) / (max - min); // [0,1]
        int index = (int) (normalized * SIZE);
        return Math.min(index, SIZE - 1);
    }

    void addToArchive(DungeonTree tree) {
        float xValue = xAxisFunction.apply(tree);
        float yValue = yAxisFunction.apply(tree);

        int xIndex = mapToIndex(xValue, min_x, max_x);
        int yIndex = mapToIndex(yValue, min_y, max_y);

        if (xIndex < 0 || yIndex < 0) {
            return;
        }

        ArchiveRecord existingRecord = archive[xIndex][yIndex];
        float newQuality = fitnessFunction.apply(tree);

        if (existingRecord == null ||
                newQuality >= existingRecord.getFitness()) {
            archive[xIndex][yIndex] = new ArchiveRecord(tree, newQuality);
        }
    }

     public DungeonTree getRandomTree() {
        int xIndex = (int)(Math.random() * SIZE);
        int yIndex = (int)(Math.random() * SIZE);
        ArchiveRecord rec = archive[xIndex][yIndex];
        if (rec != null) {
            return rec.getTree();
        }
        return getRandomTree();
    }

     public DungeonTree getRandomGoodTree() {
        int xIndex = (int)(Math.random() * SIZE);
        int yIndex = (int)(Math.random() * SIZE);
        ArchiveRecord rec = archive[xIndex][yIndex];
        if (rec != null && rec.getFitness() > 0.0f) {
            return rec.getTree();
        }
        return getRandomGoodTree();
    }

    public DungeonTree getTreeAt(int xIndex, int yIndex) {
        if (xIndex < 0 || xIndex >= SIZE || yIndex < 0 || yIndex >= SIZE) {
            throw new IndexOutOfBoundsException("Index out of bounds for archive");
        }
        ArchiveRecord rec = archive[xIndex][yIndex];
        if (rec != null) {
            return rec.getTree();
        }
        return null;
    }

    public void print() {
        for (int y = SIZE - 1; y >= 0; y--) {
            for (int x = 0; x < SIZE; x++) {
                ArchiveRecord rec = archive[x][y];
                if (rec != null) {
                    System.out.printf("[%.2f] ", rec.getFitness());
                } else {
                    System.out.print("[----] ");
                }
            }
            System.out.println();
        }
    }

    // todo: add functions like getEasyLevel(), getHardLevel(), etc.
}
