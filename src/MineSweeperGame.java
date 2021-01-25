import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MineSweeperGame {
    private final Scanner scanner;
    private final Random random = new Random();
    private final List<Mine> mines = new ArrayList<>();
    private final int [][] modifiers = {{-1, -1}, {-1,  0}, {-1,  1}, { 0, -1}, { 0,  1}, { 1, -1}, { 1,  0}, { 1,  1}};
    private final char[][] field = new char[12][12];
    private boolean isBombExploded = false;
    private boolean isGameEnded = false;

    public MineSweeperGame(Scanner scanner) {
        this.scanner = scanner;
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
        initializeField();

        if (hasMines())
            removeAllMines();

        while (getNumberOfMines() < 10) {
            int x = random.nextInt(10) + 1;
            int y = random.nextInt(10) + 1;

            Mine mine = new Mine(x, y);
            if(!mines.contains(mine))
                mines.add(mine);
        }

        boolean isSessionEnded = false;
        while (!isSessionEnded && !isBombExploded && !hasPlayerWon()) {
            drawField();
            printPlayerChoice();

            switch (getPlayerInput()) {
                case 1 -> askFlag();
                case 2 -> askTick();
                case 3 -> isSessionEnded = true;
                default -> System.out.println("Wrong input.");
            }
        }

        if (hasPlayerWon()) {
            printPlayerWon();
        }
    }
    private void initializeField() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                field[i][j] = '.';
            }
        }
    }
    private boolean hasMines() {
        return getNumberOfMines() > 0;
    }
    private int getNumberOfMines() {
        return mines.size();
    }
    private void removeAllMines() {
        mines.clear();
    }
    private void showAllBombs() {
        for (int i = 0; i < getNumberOfMines(); i++) {
            Mine mine = mines.get(i);
            int x = mine.getX();
            int y = mine.getY();

            field[x][y] = '*';
        }
    }
    private void drawField() {
        System.out.println();

        for (int i = 0; i < 10; i++) {
            field[0][i + 1] = (char) (48 + i);
        }
        for (int i = 0; i < 10; i++) {
            field[i + 1][0] = (char) (65 + i);
        }

        field[0][0] = '/';

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("");
        }

        System.out.println();
    }
    private void flag(int x, int y) {
        field[x][y] = 'F';
    }
    private void tick(int x, int y) throws ArrayIndexOutOfBoundsException {
        isBombExploded = false;

        for (int i = 0; i < getNumberOfMines(); i++) {
            Mine mine = mines.get(i);
            if (mine.getX() == x && mine.getY() == y) {
                isBombExploded = true;
                showAllBombs();
                drawField();
            }
        }

        if (isBombExploded) {
            printPlayerLost();
        } else {
            int numberOfBombsAroundTick = 0;

            for (Mine mine : mines) {
                for (int[] modifier : modifiers) {
                    int modX = x + modifier[0];
                    int modY = y + modifier[1];
                    boolean isBomb = mine.getX() == modX && mine.getY() == modY;

                    if(isBomb)
                        numberOfBombsAroundTick++;
                }
            }

            if (numberOfBombsAroundTick > 0) {
                field[x][y] = getDigitChar(numberOfBombsAroundTick);
            } else if (field[x][y] == '.') {
                field[x][y] = ' ';
                for (int[] modifier : modifiers)
                    tick(x + modifier[0], y + modifier[1]);
            }
        }
    }

    private int getNumberOfFlags() {
        int numberOfFlags = 0;

        for (int x = 1; x < 12; x++) {
            for (int y = 1; y < 12; y++) {
                if (field[x][y] == 'F')
                    numberOfFlags++;
            }
        }

        return numberOfFlags;
    }
    private boolean hasPlayerWon() {
        for (int x = 1; x < 12; x++) {
            for (int y = 1; y < 12; y++) {
                if (field[x][y] == '.' || getNumberOfFlags() != 10) {
                    return false;
                }
            }
        }
        return true;
    }

    public void askFlag() {
        int remainingFlags = 10 - getNumberOfFlags();
        System.out.println("Number of flags remaining: " + remainingFlags);
        System.out.print("Flag: ");
        String flag = scanner.next();

        if (isValidPlayerInput(flag)) { // controleren of de input toegestaan is
            int x = flag.charAt(0) - 64;
            int y = flag.charAt(1) - 47;
            flag(x, y);
        }
    }
    private void askTick() {
        String tick;
        int x = 0, y = 0;
        System.out.print("Tick: ");
        tick = scanner.next();

        if (isValidPlayerInput(tick)) {
            x = tick.charAt(0) - 64;
            y = tick.charAt(1) - 47;
            tick(x, y);
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

    // Helper methods
    private char getDigitChar(int value) {
        return (char)(value + 48);
    }
}