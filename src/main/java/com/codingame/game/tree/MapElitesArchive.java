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
    private final ArchiveRecord[][] archive;

    public MapElitesArchive(
            Function<DungeonTree, Float> xAxisFunction,
            Function<DungeonTree, Float> yAxisFunction,
            Function<DungeonTree, Float> fitnessFunction
    ) {
        archive = new ArchiveRecord[SIZE][SIZE];
        this.xAxisFunction = xAxisFunction;
        this.yAxisFunction = yAxisFunction;
        this.fitnessFunction = fitnessFunction;
    }

    public void populateArchive(int numIndividuals) {
        for (int i = 0; i < numIndividuals; i++) {
            DungeonTree tree = new DungeonTree();
            tree.generateRandomTree(
                    (int) (Constants.MIN_DEPTH + Math.random() * (Constants.MAX_DEPTH - Constants.MIN_DEPTH)),
                    (float) (0.5 + Math.random() * 0.5),
                    0.95f
            );
            addToArchive(tree);
        }
    }

    void addToArchive(DungeonTree tree) {
        float xValue = xAxisFunction.apply(tree);
        float yValue = yAxisFunction.apply(tree);
        if (xValue < 0 || yValue < 0) {
            return;
        }
        int xIndex = Math.min((int)(xValue * SIZE), SIZE - 1);
        int yIndex = Math.min((int)(yValue * SIZE), SIZE - 1);
        ArchiveRecord existingRecord = archive[xIndex][yIndex];
        float newQuality = fitnessFunction.apply(tree);
        if (existingRecord == null) {
            archive[xIndex][yIndex] = new ArchiveRecord(tree, newQuality);
        } else {
            float existingQuality = existingRecord.getFitness();
            if (newQuality >= existingQuality) {
                archive[xIndex][yIndex] = new ArchiveRecord(tree, newQuality);
            }
        }
    }

     public DungeonTree getRandomTree() {
        int xIndex = (int)(Math.random() * SIZE);
        int yIndex = (int)(Math.random() * SIZE);
        ArchiveRecord rec = archive[xIndex][yIndex];
        if (rec != null) {
            return rec.getTree();
        }
        return null;
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
