package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        final Game game = new Game(false, new HumanPlayer(), new HumanPlayer());
        int result;
        do {
            Board board = new BoardMNK(1, 1, 2, true);
            System.out.println("New game!");
            result = game.play(board);
            if (result == 0) {
                System.out.println("Game result: draw.");
            } else {
                System.out.println("Win position:");
                System.out.println(board);
                System.out.printf("Game result: player no %d win!%n", result);
            }
        } while (result != 0);
    }
}
