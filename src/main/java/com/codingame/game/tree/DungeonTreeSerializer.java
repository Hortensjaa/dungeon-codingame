package com.codingame.game.tree;


import java.io.File;
import java.io.IOException;
import java.util.*;

import com.codingame.game.move.Direction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
@NoArgsConstructor
class EdgeDTO{
    int from;
    int to;
    String direction;
}


@AllArgsConstructor
@Getter
@NoArgsConstructor
class NodeDTO{
        int id;
        String type;
        int depth;
        float difficulty;
        float reward;
}


@AllArgsConstructor
@Getter
@NoArgsConstructor
class DungeonTreeDTO {
    float fitness;
    List<NodeDTO> nodes;
    List<EdgeDTO> edges;
}


public class DungeonTreeSerializer {

    private static DungeonTreeDTO serialize(DungeonTree root, float fitness) {
        Map<DungeonTree, Integer> ids = new IdentityHashMap<>();
        List<NodeDTO> nodes = new ArrayList<>();
        List<EdgeDTO> edges = new ArrayList<>();

        assignIds(root, ids);

        for (Map.Entry<DungeonTree, Integer> entry : ids.entrySet()) {
            DungeonTree node = entry.getKey();
            int id = entry.getValue();

            nodes.add(new NodeDTO(
                    id,
                    node.getType().getName(),
                    node.getDepth(),
                    node.getType().getDifficulty(),
                    node.getType().getReward()
            ));

            addEdge(edges, ids, node, node.getLeftChild(), Direction.LEFT.getName());
            addEdge(edges, ids, node, node.getRightChild(), Direction.RIGHT.getName());
            addEdge(edges, ids, node, node.getTopChild(), Direction.UP.getName());
            addEdge(edges, ids, node, node.getBottomChild(), Direction.DOWN.getName());
        }

        return new DungeonTreeDTO(fitness, nodes, edges);
    }

    private static void assignIds(DungeonTree root, Map<DungeonTree, Integer> ids) {
        Queue<DungeonTree> queue = new ArrayDeque<>();
        queue.add(root);
        ids.put(root, 0);

        int nextId = 1;

        while (!queue.isEmpty()) {
            DungeonTree cur = queue.poll();

            for (DungeonTree child : Arrays.asList(
                    cur.getLeftChild(),
                    cur.getRightChild(),
                    cur.getTopChild(),
                    cur.getBottomChild()
            )) {
                if (child != null && !ids.containsKey(child)) {
                    ids.put(child, nextId++);
                    queue.add(child);
                }
            }
        }
    }

    private static void addEdge(
            List<EdgeDTO> edges,
            Map<DungeonTree, Integer> ids,
            DungeonTree from,
            DungeonTree to,
            String direction
    ) {
        if (to != null) {
            edges.add(new EdgeDTO(
                    ids.get(from),
                    ids.get(to),
                    direction
            ));
        }
    }

    public static DungeonTree deserialize(DungeonTreeDTO dto) {

        Map<Integer, DungeonTree> nodes = new HashMap<>();

        for (NodeDTO n : dto.getNodes()) {
            DungeonTree treeNode = new DungeonTree();
            treeNode.setType(NodeTypes.fromString(n.getType(), n.getDifficulty(), n.getReward()));
            nodes.put(n.getId(), treeNode);
        }

        Set<Integer> childrenIds = new HashSet<>();

        for (EdgeDTO e : dto.getEdges()) {
            DungeonTree from = nodes.get(e.getFrom());
            DungeonTree to = nodes.get(e.getTo());

            childrenIds.add(e.getTo());

            String dirString = e.getDirection();
            Direction direction = Direction.fromString(dirString);

            switch (direction) {
                case LEFT:
                    from.setLeftChild(to);
                    break;
                case RIGHT:
                    from.setRightChild(to);
                    break;
                case UP:
                    from.setTopChild(to);
                    break;
                case DOWN:
                    from.setBottomChild(to);
                    break;
            }

        }

        DungeonTree root = null;

        for (Integer id : nodes.keySet()) {
            if (!childrenIds.contains(id)) {
                root = nodes.get(id);
                break;
            }
        }

        if (root == null) {
            throw new IllegalStateException("No root node found in DungeonTreeDTO");
        }

        recomputeDepth(root, 0);

        return root;
    }

    private static void recomputeDepth(DungeonTree node, int depth) {
        node.setDepth(depth);

        if (node.getLeftChild() != null)
            recomputeDepth(node.getLeftChild(), depth + 1);
        if (node.getRightChild() != null)
            recomputeDepth(node.getRightChild(), depth + 1);
        if (node.getTopChild() != null)
            recomputeDepth(node.getTopChild(), depth + 1);
        if (node.getBottomChild() != null)
            recomputeDepth(node.getBottomChild(), depth + 1);
    }

    // ------------------ API ------------------
    public static void writeToFile(DungeonTree tree, File file) throws IOException {
        writeToFile(tree, -1.0f, file);
    }

    public static void writeToFile(DungeonTree tree, float fitness, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);

        mapper.writeValue(file, serialize(tree, fitness));
    }

    public static DungeonTree readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DungeonTreeDTO dto = mapper.readValue(file, DungeonTreeDTO.class);

        return deserialize(dto);
    }

//    ------------------- test ------------------
public static void main(String[] args) throws Exception {
    DungeonTree original = new DungeonTree();
    original.generateRandomTree(4, 0.8f, 0.9f);

    File file = new File("levels/dungeon_tree2.json");

    DungeonTreeSerializer.writeToFile(original, file);

    DungeonTree loaded = readFromFile(file);

    System.out.println("Original nodes: " + original.countNodes());
    System.out.println("Loaded nodes:   " + loaded.countNodes());

    System.out.println("Has start+exit: " + loaded.hasStartAndExit());
}

}

