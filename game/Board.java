package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Board {
    Position getPosition();
    Cell getCell();
    MoveBoardResult makeMove(Move move);
}
