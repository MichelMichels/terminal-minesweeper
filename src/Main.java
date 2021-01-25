import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner kb = new Scanner(System.in);
    static MineList l = new MineList();
    static Random random = new Random();

    public static void main(String[] args) {
        boolean exit = false;
        do {
            spelMenu();
            int id = 0;

            try {
                id = kb.nextInt();
            } catch (Exception e) {
                System.out.println("Chars are forbidden.");
                kb.next();
            }


            switch (id) {
                case 1:
                    newGame();
                    break;
                case 2:
                    credits();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Bye Bye");
                    break;
                default:
                    System.out.println("Use 1 or 2, you hacker.");
                    break;
            }

        } while (!exit);
    }

    public static void spelMenu() {
        System.out.println();
        System.out.println("* MINESWEEPER *");
        System.out.println();
        System.out.println("\t1. Start the game");
        System.out.println("\t2. Credits");
        System.out.println("\t3. Exit");
    }

    public static void newGame() {
        l.initializeField();

        if (l.getSize() > 0) {
            l.removeAllMines();
        }

        while (l.getSize() != 10) {
            int x = random.nextInt(10) + 1;
            int y = random.nextInt(10) + 1;

            Mine m = new Mine(x, y);
            l.addMines(m);
        }

        boolean stop = false;
        do {
            l.writeField();
            System.out.println("\t1. Flag  2. Tick Field 3. Exit");
            int id = kb.nextInt();
            switch (id) {
                case 1:
                    flag();
                    break;
                case 2:
                    tick();
                    break;
                case 3:
                    stop = true;
                    break;
                //case 4: l.showAllBombs(); break;
                default:
                    System.out.println("Wrong input.");
                    break;
            }

            //System.out.println(id);

        } while (!stop && !l.getStop() && !l.gewonnen());

        if (l.gewonnen()) {
            System.out.println();
            System.out.println("Proficiat U heeft gewonnen! :D");
            System.out.println();
        }
    }

    public static void flag() {
        String flag;
        int x = 0, y = 0;
        int remFlags = 10 - l.numberOfFlags();
        System.out.println("Number of flags remaining: " + remFlags);
        System.out.print("Flag: ");
        flag = kb.next();

        if (checkInput(flag)) { // controleren of de input toegestaan is
            x = flag.charAt(0) - 64;
            y = flag.charAt(1) - 47;
            l.flag(x, y);
        }
    }

    public static void tick() {
        String tick;
        int x = 0, y = 0;
        System.out.print("Tick: ");
        tick = kb.next();

        if (checkInput(tick)) {
            x = tick.charAt(0) - 64;
            y = tick.charAt(1) - 47;
            l.tick(x, y);
        }
    }

    public static boolean checkInput(String s) {
        boolean check = true;

        if (s.length() == 2) {
            if (s.charAt(0) < 65 || s.charAt(0) > 74) {
                System.out.println();
                System.out.println("Incorrecte invoer.");
                check = false;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static void credits() {
        System.out.println();
        System.out.println(" *********************************");
        System.out.println(" *   WRITTEN BY MICHEL MICHELS   *");
        System.out.println(" *          v2.0 \u00A9 2014          *");
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
}

