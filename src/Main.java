import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MineSweeperGame game = new MineSweeperGame(new Scanner(System.in));
        game.run();
    }
}