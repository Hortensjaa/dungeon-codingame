package com.codingame.game.generators;

import com.codingame.game.move.Action;
import com.codingame.game.Constants;

import java.util.ArrayDeque;

public class Utils {
    public static class Result {
        int x, y, distance;

        Result(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

    public static Result bfsFarthest(int[][] grid, int sx, int sy) {
        int h = grid.length;
        int w = grid[0].length;

        boolean[][] visited = new boolean[h][w];
        ArrayDeque<int[]> q = new ArrayDeque<>();

        q.add(new int[]{sx, sy, 0});
        visited[sy][sx] = true;

        int fx = sx, fy = sy, maxDist = 0;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int d = cur[2];

            if (d > maxDist) {
                maxDist = d;
                fx = x;
                fy = y;
            }

            for (Action a : Action.values()) {
                if (a == Action.STAY) continue;

                int nx = x + a.getDx();
                int ny = y + a.getDy();

                if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                if (grid[ny][nx] == Constants.WALL) continue;
                if (visited[ny][nx]) continue;

                visited[ny][nx] = true;
                q.add(new int[]{nx, ny, d + 1});
            }
        }

        return new Result(fx, fy, maxDist);
    }
}
