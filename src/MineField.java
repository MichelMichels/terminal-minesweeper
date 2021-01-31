import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineField {
    private final Random random = new Random();
    private final int numberOfRows;
    private final int numberOfColumns;
    private final char[][] field;
    private final List<Mine> mines = new ArrayList<>();
    private final int [][] modifiers = {
        {-1, -1},
        {-1,  0},
        {-1,  1},
        { 0, -1},
        { 0,  1},
        { 1, -1},
        { 1,  0},
        { 1,  1}
    };

    public MineField(int size) {
        numberOfColumns = size;
        numberOfRows = size;

        field = new char[numberOfColumns][numberOfRows];
    }

    // Public methods
    public void initialize() {
        clearField();
        if (hasMines()) {
            removeAllMines();
        }
        generateMines(10);
    }
    public void draw() {
        System.out.println();

        String headerRow = generateHeaderRow();
        System.out.println(headerRow);

        for (int y = 0; y < numberOfRows; y++) {
            System.out.print((char)('0' + y));
            System.out.print(" ");

            for (int x = 0; x < numberOfColumns; x++) {
                System.out.print(field[x][y] + " ");
            }
            System.out.println("");
        }

        System.out.println();
    }
    public void flag(int x, int y) {
        field[x][y] = 'F';
    }
    public boolean tick(int x, int y) throws ArrayIndexOutOfBoundsException {
        System.out.println("[DEBUG] Ticked:  X = " + x + " Y = " + y);
        if(!isValidInput(x, y)) {
            System.out.println("Invalid input");
            return false;
        }

        if(isAnyBombTicked(x, y)) {
            System.out.println("Bomb is ticked");
            showAllBombs();
            draw();
            return true;
        }

        int numberOfMinesAroundTick = getNumberOfMinesAroundPosition(x, y);
        boolean hasAnyMines = numberOfMinesAroundTick > 0;
        if (hasAnyMines) {
            System.out.println("Number on place ticked written");
            writeNumberOfMines(numberOfMinesAroundTick, x, y);
        } else if (isPositionDefault(x, y)) {
            System.out.println("Empty on place ticked written");
            writeEmpty(x, y);
            for (int[] modifier : modifiers) {
                if (tick(x + modifier[0], y + modifier[1])) {
                    return true;
                }
            }
        }

        return false;
    }

    // Private methods
    private void clearField() {
        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                field[x][y] = '.';
            }
        }
    }
    private boolean hasMines() {
        return getNumberOfMines() > 0;
    }
    private void generateMines(int quantity) {
        while (getNumberOfMines() < quantity) {
            int x = random.nextInt(numberOfColumns);
            int y = random.nextInt(numberOfRows);

            Mine mine = new Mine(x, y);
            if(!mines.contains(mine)) {
                mines.add(mine);
            }
        }
    }
    private int getNumberOfMines() {
        return mines.size();
    }
    private void removeAllMines() {
        mines.clear();
    }
    private void showAllBombs() {
        for(Mine mine : mines) {
            int x = mine.getX();
            int y = mine.getY();

            field[x][y] = '*';
        }
    }
    public int getNumberOfFlags() {
        int numberOfFlags = 0;

        for (int x = 1; x < numberOfColumns; x++) {
            for (int y = 1; y < numberOfColumns; y++) {
                if (field[x][y] == 'F')
                    numberOfFlags++;
            }
        }

        return numberOfFlags;
    }
    public boolean areAllMinesFlagged() {
        if(getNumberOfFlags() != getNumberOfMines()) {
            return false;
        }

        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                if (isPositionDefault(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
    private String generateHeaderRow() {
        StringBuilder headerRow = new StringBuilder("/ ");
        for (int x = 0; x < numberOfColumns; x++) {
            headerRow.append((char) ('A' + x));
            headerRow.append(' ');
        }

        return headerRow.toString();
    }
    private boolean isAnyBombTicked(int x, int y) {
        for (Mine mine : mines) {
            boolean isBombExploded = (mine.getX() == x && mine.getY() == y);
            if (isBombExploded) {
                System.out.println("Bomb exploded");
                return true;
            }
        }

        return false;
    }
    private boolean isValidInput(int x, int y) {
        return
            x >= 0
            && y >= 0
            && x < numberOfColumns
            && y < numberOfRows;
    }
    private int getNumberOfMinesAroundPosition(int x, int y) {
        int numberOfMines = 0;
        for (Mine mine : mines) {
            for (int[] modifier : modifiers) {
                int modifiedX = x + modifier[0];
                int modifiedY = y + modifier[1];

                if(isMine(mine, modifiedX, modifiedY)) {
                    numberOfMines++;
                }
            }
        }

        return numberOfMines;
    }
    private boolean isMine(Mine mine, int x, int y) {
        return mine.getX() == x && mine.getY() == y;
    }
    private void writeNumberOfMines(int quantity, int x, int y) {
        field[x][y] = getDigitChar(quantity);
    }
    private void writeEmpty(int x, int y) {
        field[x][y] = ' ';
    }
    private boolean isPositionDefault(int x, int y) {
        return field[x][y] == '.';
    }
    private boolean isPositionEmpty(int x, int y) {
        return field[x][y] == ' ';
    }

    // Helper methods
    private char getDigitChar(int value) {
        return (char)(value + 48);
    }
}
