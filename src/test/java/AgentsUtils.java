public class AgentsUtils {
    public static final String[] DIRECTIONS = {"RIGHT", "DOWN", "LEFT", "UP"};
    public static final String[] ACTIONS = {"RIGHT", "DOWN", "LEFT", "UP", "STAY"};

    public static final int[] DX = {1, 0, -1, 0};
    public static final int[] DY = {0, 1, 0, -1};

    public static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
