import java.util.Scanner;

public class GreedyAgent {
    private int width, height;
    private int exitX, exitY;
    private int playerX, playerY;
    private int lastPlayerX = -1, lastPlayerY = -1;
    private int currentAltDir = 0;
    private int[][] grid;

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

            lastPlayerX = playerX;
            lastPlayerY = playerY;
        }
    }

    private String decide() {
        if (playerX == lastPlayerX && playerY == lastPlayerY && lastPlayerX != -1) {
            currentAltDir = (currentAltDir + 1) % AgentsUtils.DIRECTIONS.length;
            return AgentsUtils.DIRECTIONS[currentAltDir];
        }

        int dx = exitX - playerX;
        int dy = exitY - playerY;

        // Prefer the axis with greater distance
        if (Math.abs(dx) >= Math.abs(dy)) {
            if (dx > 0) return "RIGHT";
            if (dx < 0) return "LEFT";
            if (dy > 0) return "DOWN";
            if (dy < 0) return "UP";
        } else {
            if (dy > 0) return "DOWN";
            if (dy < 0) return "UP";
            if (dx > 0) return "RIGHT";
            if (dx < 0) return "LEFT";
        }

        return "STAY";
    }

    public static void main(String[] args) {
        new GreedyAgent().run();
    }
}
