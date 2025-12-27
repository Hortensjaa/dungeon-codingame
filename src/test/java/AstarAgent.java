import com.codingame.game.move.Action;
import com.codingame.game.Constants;

import java.util.*;

public class AstarAgent {

    private int width, height;
    private int exitX, exitY;
    private int playerX, playerY;
    private int[][] grid;

    // ==== tuning ====
    private static final int ENEMY_PENALTY = 15;
    private static final int REWARD_HEURISTIC_BONUS = 6;
    private static final int MIN_HEURISTIC = 0;

    static class Node {
        int x, y;
        int g, h;
        Node parent;
        Action actionFromParent;

        Node(int x, int y, int g, int h, Node parent, Action actionFromParent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
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

        grid = new int[height][width];
        for (int y = 0; y < height; y++) {
            String row = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                grid[y][x] = row.charAt(x) - '0';
            }
        }

        while (true) {
            String[] posParts = scanner.nextLine().split(" ");
            playerX = Integer.parseInt(posParts[0]);
            playerY = Integer.parseInt(posParts[1]);

            System.out.println(decide());
        }
    }

    // === Manhattan heuristic ===
    private int manhattan(int x, int y) {
        return Math.abs(exitX - x) + Math.abs(exitY - y);
    }

    private int heuristic(int x, int y) {
        int h = manhattan(x, y);

        // slight attraction to reward
        if (grid[y][x] == Constants.REWARD) {
            h -= REWARD_HEURISTIC_BONUS;
        }

        return Math.max(MIN_HEURISTIC, h);
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

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(Node::f));
        boolean[][] closed = new boolean[height][width];
        int[][] bestG = new int[height][width];

        for (int[] row : bestG)
            Arrays.fill(row, Integer.MAX_VALUE);

        Node start = new Node(
                playerX,
                playerY,
                0,
                heuristic(playerX, playerY),
                null,
                Action.STAY
        );

        open.add(start);
        bestG[playerY][playerX] = 0;

        Node goal = null;

        while (!open.isEmpty()) {
            Node cur = open.poll();

            if (closed[cur.y][cur.x])
                continue;

            closed[cur.y][cur.x] = true;

            if (cur.x == exitX && cur.y == exitY) {
                goal = cur;
                break;
            }

            for (Action action : Action.values()) {
                if (action == Action.STAY)
                    continue;

                int nx = cur.x + action.getDx();
                int ny = cur.y + action.getDy();

                if (nx < 0 || ny < 0 || nx >= width || ny >= height)
                    continue;

                if (grid[ny][nx] == Constants.WALL)
                    continue;

                int stepCost = 1;

                if (grid[ny][nx] == Constants.ENEMY)
                    stepCost += ENEMY_PENALTY;

                int newG = cur.g + stepCost;

                if (newG >= bestG[ny][nx])
                    continue;

                bestG[ny][nx] = newG;

                Node next = new Node(
                        nx,
                        ny,
                        newG,
                        heuristic(nx, ny),
                        cur,
                        action
                );

                open.add(next);
            }
        }

        if (goal == null)
            return "STAY";

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
