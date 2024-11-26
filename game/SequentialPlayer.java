package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SequentialPlayer implements Player {
    @Override
    public Move move(final Position position, final Cell cell) {
        for (int r = 0; r < position.getRows(); r++) {
            for (int c = 0; c < position.getColumns(); c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }

    @Override
    public boolean drawInvite() {
        return false;
    }

    @Override
    public String getName() {
        return "Sequential Player";
    }

    @Override
    public String toString() {
        return getName();
    }
}
