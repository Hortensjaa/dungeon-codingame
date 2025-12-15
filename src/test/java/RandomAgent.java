import java.util.Scanner;

public class RandomAgent {
    private static final float p_turn_increase = 0.05f;

    private void run() {
        Scanner scanner = new Scanner(System.in);
        float p_turn = 0f;
        int cur_dir_id = 0;

        while (true) {
            String input = scanner.nextLine();
            p_turn += p_turn_increase;
            if (Math.random() < p_turn) {
                p_turn = 0f;
                cur_dir_id = (cur_dir_id + 1) % AgentsUtils.DIRECTIONS.length;
            }
            System.out.println(AgentsUtils.DIRECTIONS[cur_dir_id]);
        }
    }

    public static void main(String[] args) {
        new RandomAgent().run();
    }
}
