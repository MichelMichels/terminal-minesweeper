import java.util.Random;
import java.util.Scanner;

public class MineSweeperGame {
    private final Scanner scanner;
    private final Random random = new Random();
    private boolean isBombExploded = false;
    private boolean isGameEnded = false;
    private final MineField mineField;

    public MineSweeperGame(Scanner scanner) {
        this.scanner = scanner;

        mineField = new MineField(10);
    }

    public void run() {
        while (!isGameEnded) {
            printMenu();

            switch (getPlayerInput()) {
                case 1 -> start();
                case 2 -> printCredits();
                case 3 -> isGameEnded = true;
                default -> System.out.println("Use 1 or 2, you hacker.");
            }
        }

        System.out.println("Bye Bye");
    }

    private void start() {
        mineField.initialize();
        isBombExploded = false;

        boolean isSessionEnded = false;
        while (!isSessionEnded && !isBombExploded && !mineField.areAllMinesFlagged()) {
            mineField.draw();
            printPlayerChoice();

            switch (getPlayerInput()) {
                case 1 -> askPlayerFlag();
                case 2 -> askPlayerTick();
                case 3 -> isSessionEnded = true;
                default -> System.out.println("Wrong input.");
            }
        }

        if (mineField.areAllMinesFlagged()) {
            printPlayerWon();
            isSessionEnded = true;
        }

        if(isBombExploded) {
            printPlayerLost();
            isSessionEnded = true;
        }

    }

    public void askPlayerFlag() {
        int remainingFlags = 10 - mineField.getNumberOfFlags();
        System.out.println("Number of flags remaining: " + remainingFlags);
        System.out.print("Flag: ");
        String flag = scanner.next();

        if (isValidPlayerInput(flag)) {
            int x = flag.charAt(0) - 65;
            int y = flag.charAt(1) - 48;
            mineField.flag(x, y);
        }
    }
    private void askPlayerTick() {
        System.out.print("Tick: ");
        String input = scanner.next();

        if (isValidPlayerInput(input)) {
            int x = input.charAt(0) - 65;
            int y = input.charAt(1) - 48;

            if(mineField.tick(x, y)) {
                isBombExploded = true;
            }
        }
    }
    private boolean isValidPlayerInput(String input) {
        if (input.length() != 2) {
            System.out.println();
            System.out.println("Incorrecte invoer.");
            return false;
        }

        if (input.charAt(0) < 65 || input.charAt(0) > 74) {
            System.out.println();
            System.out.println("Incorrecte invoer.");
            return false;
        }

        return true;
    }
    private int getPlayerInput() {
        try {
            return scanner.nextInt();
        } catch(Exception ignored) {
            return 0;
        }
    }

    // Print methods
    private void printMenu() {
        System.out.println();
        System.out.println("* MINESWEEPER *");
        System.out.println();
        System.out.println("\t1. Start the game");
        System.out.println("\t2. Credits");
        System.out.println("\t3. Exit");
    }
    private void printPlayerChoice() {
        System.out.println("\t1. Flag  2. Tick Field 3. Exit");
    }
    private void printCredits() {
        System.out.println();
        System.out.println(" *********************************");
        System.out.println(" *   WRITTEN BY MICHEL MICHELS   *");
        System.out.println(" *          v3.0 \u00A9 2021          *");
        System.out.println(" *                               *");
        System.out.println(" *  I thank following people:    *");
        System.out.println(" *            Anne M.            *");
        System.out.println(" *            Mike M.            *");
        System.out.println(" *            Liese M.           *");
        System.out.println(" *            Emma C. \u2665          *");
        System.out.println(" *                               *");
        System.out.println(" *********************************");
        System.out.println();
    }
    private void printPlayerWon() {
        System.out.println();
        System.out.println("Proficiat U heeft gewonnen! :D");
        System.out.println();
    }
    private void printPlayerLost() {
        System.out.println();
        System.out.println("*** GAME OVER ***");
        System.out.println();
    }


}