import com.codingame.game.move.Action;
import com.codingame.game.Constants;

import java.util.Scanner;
import java.util.*;

public class AstarAgent {
    private int width, height;
    private int exitX, exitY;
    private int playerX, playerY;
    private int[][] grid;

    static class Node {
        int x, y;
        int g, h;
        Node parent;
        Action actionFromParent;

        Node(int x, int y, int g, int h, Node parent, Action actionFromParent) {
            this.x = x;
            this.y = y;
            this.g = g;  // cost from start to current node
            this.h = h;  // heuristic cost from current node to goal
            this.parent = parent;
            this.actionFromParent = actionFromParent;
        }

        int f() {
            return g + h;
        }
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        String[] dims = scanner.nextLine().split(" ");
        width = Integer.parseInt(dims[0]);
        height = Integer.parseInt(dims[1]);

        String[] exitParts = scanner.nextLine().split(" ");
        exitX = Integer.parseInt(exitParts[0]);
        exitY = Integer.parseInt(exitParts[1]);

        // Read grid (0 = wall, 1 = floor)
        grid = new int[height][width];
        for (int y = 0; y < height; y++) {
            String row = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                grid[y][x] = row.charAt(x) - '0';
            }
        }

        while (true) {
            // Read turn: PLAYER_X PLAYER_Y
            String[] posParts = scanner.nextLine().split(" ");
            playerX = Integer.parseInt(posParts[0]);
            playerY = Integer.parseInt(posParts[1]);

            String action = decide();
            System.out.println(action);
        }
    }

    private int manhattan(int x, int y) {
        return Math.abs(exitX - x) + Math.abs(exitY - y);
    }

    private String direction(int x1, int y1, int x2, int y2) {
        if (x2 == x1 + 1) return "RIGHT";
        if (x2 == x1 - 1) return "LEFT";
        if (y2 == y1 + 1) return "DOWN";
        if (y2 == y1 - 1) return "UP";
        return "STAY";
    }

    private String decide() {
        if (playerX == exitX && playerY == exitY) {
            return "STAY";
        }

        Node start = new Node(playerX, playerY, 0,
                manhattan(playerX, playerY), null, Action.STAY);

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(Node::f));
        boolean[][] closed = new boolean[height][width];

        open.add(start);

        Node goal = null;

        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (closed[cur.y][cur.x]) continue;
            closed[cur.y][cur.x] = true;

            if (cur.x == exitX && cur.y == exitY) {
                goal = cur;
                break;
            }

            for (Action action : Action.values()) {
                if (action == Action.STAY) continue;

                int nx = cur.x + action.getDx();
                int ny = cur.y + action.getDy();

                if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;
                if (grid[ny][nx] == Constants.WALL) continue; // wall
                if (closed[ny][nx]) continue;

                Node next = new Node(
                        nx,
                        ny,
                        cur.g + 1,
                        manhattan(nx, ny),
                        cur,
                        action
                );
                open.add(next);
            }
        }

        Node step = goal;
        while (step.parent != null && step.parent.parent != null) {
            step = step.parent;
        }

        return direction(playerX, playerY, step.x, step.y);
    }


    public static void main(String[] args) {
        new AstarAgent().run();
    }
}
