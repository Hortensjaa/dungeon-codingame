package com.codingame.game;

import com.codingame.game.algorithm.MapElites;
import com.codingame.game.algorithm.MapElitesArchive;
import com.codingame.game.generator.GeneratorFromLayout;
import com.codingame.game.generator.GridDefinition;
import com.codingame.game.tree.DungeonTree;

public final class GenerationUtils {
    static final int MAX_RETRIES = 20;

    public static GridDefinition generateFromFile(String dirName, int x, int y) {
        return GeneratorFromLayout.generate(dirName, x, y, MAX_RETRIES);
    }

    public static GridDefinition runAndGenerate(int numGenerations) {
         MapElitesArchive res = MapElites.run(numGenerations);
         DungeonTree tree = res.getRandomGoodTree();
         return GeneratorFromLayout.generate(tree, MAX_RETRIES);
    }

    public static GridDefinition runAndGenerate(int numGenerations, int x, int y) {
         MapElitesArchive res = MapElites.run(numGenerations);
         DungeonTree tree = res.getTreeAt(x, y);
         return GeneratorFromLayout.generate(tree, MAX_RETRIES);
    }

    public static GridDefinition runSaveAndGenerate(int numGenerations) {
         MapElitesArchive res = MapElites.run(numGenerations);
         res.serializeArchive(numGenerations);
         DungeonTree tree = res.getRandomGoodTree();
         GridDefinition gridDefinition = GeneratorFromLayout.generate(tree, MAX_RETRIES);
         return gridDefinition;
    }

    public static GridDefinition runSaveAndGenerate(int numGenerations, int x, int y) {
        MapElitesArchive res = MapElites.run(numGenerations);
        res.serializeArchive(numGenerations);
        DungeonTree tree = res.getTreeAt(x, y);
        GridDefinition gridDefinition = GeneratorFromLayout.generate(tree, MAX_RETRIES);
        return gridDefinition;
    }

    public static GridDefinition generateFromTree(DungeonTree tree) {
        return GeneratorFromLayout.generate(tree, MAX_RETRIES);
    }

    public static GridDefinition debugSerialization(int numGenerations, int x, int y) {
        MapElitesArchive res = MapElites.run(numGenerations);
        String dirName = res.serializeArchive(numGenerations);
        // get tree directly from archive
        DungeonTree tree = res.getTreeAt(x, y);
        GridDefinition gridDefinition = GeneratorFromLayout.generate(tree, MAX_RETRIES);
        // get tree from saved files
//        GridDefinition gridDefinition2 = GeneratorFromLayout.generate(dirName, x, y);
        return gridDefinition;
    }
}
