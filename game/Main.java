package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    public static void main(String[] args) {
        final Game game = new Game(false, new HumanPlayer(), new HumanPlayer());
        GameResult result;
        do {
            Board board = new BoardMNK(6, 6, 2, true);
            System.out.println("New game!");
            result = game.play(board);
            if (result == GameResult.Draw) {
                System.out.println("Game result: draw.");
            } else {
                System.out.println("Win position:");
                System.out.println(board);
                System.out.printf("Game result: player no %d win!%n", result.value);
            }
        } while (result != GameResult.Draw);
    }
}
