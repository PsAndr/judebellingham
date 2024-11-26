package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static void skipLine() {
        try {
            scanner.nextLine();
        } catch (RuntimeException ignored) {
        }
    }

    private static int intInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (RuntimeException ignored) {
                skipLine();
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        int r = intInput("Number of rows: ");
        int c = intInput("Number of columns: ");
        int winCnt = intInput("Count to win: ");
        boolean rhombus = intInput("Need rhombus[0/1](0 - no, 1 - yes): ") == 1;
        int cntPlayers = intInput("Number of players: ");
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < cntPlayers; i++) {
            int tpPlayer = intInput("Player " + (i + 1) + " type (1 - human, 2 - random, 3 - sequential): ");
            switch (tpPlayer) {
                case 1:
                    players.add(new HumanPlayer());
                    break;
                case 2:
                    players.add(new RandomPlayer());
                    break;
                case 3:
                    players.add(new SequentialPlayer());
                    break;
            }
        }
        final SwissSystem system = new SwissSystem(new BoardMNK(r, c, winCnt, rhombus), players);
        system.start();

        /*final Game game = new Game(false, new HumanPlayer(), new HumanPlayer());
        GameResult result;
        do {
            Board board = new BoardMNK(4, 4, 2, true);
            System.out.println("New game!");
            result = game.play(board);
            if (result == GameResult.Draw) {
                System.out.println("Game result: draw.");
            } else {
                System.out.println("Win position:");
                System.out.println(board);
                System.out.printf("Game result: player no %d win!%n", result.value);
            }
        } while (result != GameResult.Draw);*/
    }
}
