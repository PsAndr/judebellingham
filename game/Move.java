package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Move {
    public static final Move DRAW = new Move(-1, -1, Cell.E, MoveType.Draw);
    public static final Move GIVE_UP = new Move(-1, -1, Cell.E, MoveType.GiveUp);

    private final MoveType moveType;

    private final int row;
    private final int column;
    private final Cell value;

    public Move(final int row, final int column, final Cell value) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.moveType = MoveType.Base;
    }

    public Move(final int row, final int column, final Cell value, final MoveType moveType) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.moveType = moveType;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell getValue() {
        return value;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public String toString() {
        return "row=" + row + ", column=" + column + ", value=" + value + ", moveType=" + moveType;
    }
}
