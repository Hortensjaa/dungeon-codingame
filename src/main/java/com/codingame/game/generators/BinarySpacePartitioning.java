package com.codingame.game.generators;

import com.codingame.game.Constants;
import com.codingame.game.Coord;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Space Partitioning dungeon generator.
 * <p>
 * Recursively splits the space into smaller partitions, creates rooms
 * in leaf nodes, and connects them with corridors.
 */
public class BinarySpacePartitioning extends Generator {
    
    private static final int MIN_PARTITION_SIZE = 5;
    private static final int MIN_ROOM_SIZE = 3;
    private static final int ROOM_PADDING = 1;
    
    private int[][] grid;
    private List<Room> rooms;
    
    @Override
    public GridDefinition generate(int rows, int columns) {
        grid = initialGridWalls(rows, columns);
        rooms = new ArrayList<>();
        
        // Create root partition and recursively split
        Partition root = new Partition(0, 0, columns, rows);
        split(root);
        
        // Create rooms in leaf partitions
        createRooms(root);
        
        // Connect rooms with corridors
        connectPartitions(root);
        
        // Place player and goal in random rooms
        Room playerRoom = rooms.get((int) (Math.random() * rooms.size()));
        Room goalRoom = rooms.get((int) (Math.random() * rooms.size()));
        
        // Ensure player and goal are in different rooms if possible
        if (rooms.size() > 1) {
            while (goalRoom == playerRoom) {
                goalRoom = rooms.get((int) (Math.random() * rooms.size()));
            }
        }
        
        Coord playerCoord = playerRoom.randomPointInside();
        Coord goalCoord = goalRoom.randomPointInside();
        
        return GridDefinition.builder()
                .grid(grid)
                .playerStart(playerCoord)
                .exit(goalCoord)
                .build();
    }
    
    /**
     * Recursively splits a partition into two smaller partitions.
     */
    private void split(Partition partition) {
        // Stop if partition is too small
        if (partition.width < MIN_PARTITION_SIZE * 2 && partition.height < MIN_PARTITION_SIZE * 2) {
            return;
        }

        // Decide split direction based on shape
        boolean splitHorizontally;
        if (partition.width < MIN_PARTITION_SIZE * 2) {
            splitHorizontally = true;
        } else if (partition.height < MIN_PARTITION_SIZE * 2) {
            splitHorizontally = false;
        } else {
            splitHorizontally = Math.random() < 0.5;
        }
        
        if (splitHorizontally) {
            // Split horizontally (top and bottom)
            int splitY = MIN_PARTITION_SIZE + (int) (Math.random() * (partition.height - MIN_PARTITION_SIZE * 2));
            partition.left = new Partition(partition.x, partition.y, partition.width, splitY);
            partition.right = new Partition(partition.x, partition.y + splitY, partition.width, partition.height - splitY);
        } else {
            // Split vertically (left and right)
            int splitX = MIN_PARTITION_SIZE + (int) (Math.random() * (partition.width - MIN_PARTITION_SIZE * 2));
            partition.left = new Partition(partition.x, partition.y, splitX, partition.height);
            partition.right = new Partition(partition.x + splitX, partition.y, partition.width - splitX, partition.height);
        }
        
        // Recursively split children
        split(partition.left);
        split(partition.right);
    }
    
    /**
     * Creates rooms in leaf partitions.
     */
    private void createRooms(Partition partition) {
        if (partition.left != null && partition.right != null) {
            createRooms(partition.left);
            createRooms(partition.right);
        } else {
            // Leaf node - create a room
            int roomWidth = MIN_ROOM_SIZE + (int) (Math.random() * (partition.width - MIN_ROOM_SIZE - ROOM_PADDING * 2));
            int roomHeight = MIN_ROOM_SIZE + (int) (Math.random() * (partition.height - MIN_ROOM_SIZE - ROOM_PADDING * 2));
            
            int roomX = partition.x + ROOM_PADDING + (int) (Math.random() * (partition.width - roomWidth - ROOM_PADDING * 2));
            int roomY = partition.y + ROOM_PADDING + (int) (Math.random() * (partition.height - roomHeight - ROOM_PADDING * 2));
            
            Room room = new Room(roomX, roomY, roomWidth, roomHeight);
            partition.room = room;
            rooms.add(room);
            
            // Carve out the room
            for (int y = room.y; y < room.y + room.height; y++) {
                for (int x = room.x; x < room.x + room.width; x++) {
                    if (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                        grid[y][x] = Constants.FLOOR;
                    }
                }
            }
        }
    }
    
    /**
     * Connects partitions by creating corridors between their rooms.
     */
    private void connectPartitions(Partition partition) {
        if (partition.left == null || partition.right == null) {
            return;
        }
        
        connectPartitions(partition.left);
        connectPartitions(partition.right);
        
        // Connect left and right subtrees
        Room leftRoom = getRandomRoom(partition.left);
        Room rightRoom = getRandomRoom(partition.right);
        
        if (leftRoom != null && rightRoom != null) {
            createCorridor(leftRoom.centerX(), leftRoom.centerY(), 
                          rightRoom.centerX(), rightRoom.centerY());
        }
    }
    
    /**
     * Gets a room from a partition (or any of its descendants).
     */
    private Room getRandomRoom(Partition partition) {
        List<Room> collected = new ArrayList<>();
        collectRooms(partition, collected);
        if (collected.isEmpty()) return null;
        return collected.get((int)(Math.random() * collected.size()));
    }

    private void collectRooms(Partition p, List<Room> out) {
        if (p == null) return;
        if (p.room != null) {
            out.add(p.room);
        }
        collectRooms(p.left, out);
        collectRooms(p.right, out);
    }
    
    /**
     * Creates an L-shaped corridor between two points.
     */
    private void createCorridor(int x1, int y1, int x2, int y2) {
        // Randomly choose whether to go horizontal-first or vertical-first
        System.out.println("Creating corridor");
        if (Math.random() < 0.5) {
            createHorizontalCorridor(x1, x2, y1);
            createVerticalCorridor(y1, y2, x2);
        } else {
            createVerticalCorridor(y1, y2, x1);
            createHorizontalCorridor(x1, x2, y2);
        }
    }
    
    private void createHorizontalCorridor(int x1, int x2, int y) {
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        for (int x = startX; x <= endX; x++) {
            if (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                grid[y][x] = Constants.FLOOR;
            }
        }
    }
    
    private void createVerticalCorridor(int y1, int y2, int x) {
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        for (int y = startY; y <= endY; y++) {
            if (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                grid[y][x] = Constants.FLOOR;
            }
        }
    }
    
    /**
     * Represents a partition in the BSP tree.
     */
    private static class Partition {
        int x, y, width, height;
        Partition left, right;
        Room room;
        
        Partition(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    /**
     * Represents a room within a partition.
     */
    private static class Room {
        int x, y, width, height;
        
        Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        
        int centerX() {
            return x + width / 2;
        }
        
        int centerY() {
            return y + height / 2;
        }
        
        Coord randomPointInside() {
            int px = x + 1 + (int) (Math.random() * (width - 2));
            int py = y + 1 + (int) (Math.random() * (height - 2));
            // Clamp to ensure we're inside
            px = Math.max(x, Math.min(x + width - 1, px));
            py = Math.max(y, Math.min(y + height - 1, py));
            return new Coord(px, py);
        }
    }
}
