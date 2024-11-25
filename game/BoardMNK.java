package game;

import java.util.Arrays;
import java.util.Map;

public class BoardMNK implements Board {
    private class PositionMNK implements Position {
        @Override
        public boolean isValid(final Move move) {
            if (move.getMoveType() == MoveType.GiveUp || move.getMoveType() == MoveType.Draw) {
                return true;
            }
            return BoardMNK.this.isValid(move.getRow(), move.getColumn())
                    && cells[move.getRow()][move.getColumn()] == Cell.E &&
                    move.getValue() == BoardMNK.this.getCell();
        }

        @Override
        public Cell getCell(final int r, final int c) {
            return cells[r][c];
        }

        @Override
        public int getRows() {
            return rows;
        }

        @Override
        public int getColumns() {
            return cols;
        }

        @Override
        public String toString() {
            return BoardMNK.this.toString();
        }
    }

    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final PositionMNK position;

    private final Cell[][] cells;
    private final int rows;
    private final int cols;
    private final int winCount;

    private final boolean isRhombus;

    private int countCells;

    private Cell turn;

    public BoardMNK(int rows, int cols, int winCount) {
        this(rows, cols, winCount, false);
    }

    public BoardMNK(int countRows, int countColumns, int count2Win, boolean isRhombus) {
        if (countRows <= 0 || countColumns <= 0 || count2Win <= 0) {
            throw new IllegalArgumentException();
        }

        if (isRhombus && countRows != countColumns) {
            throw new IllegalArgumentException();
        }

        this.isRhombus = isRhombus;

        if (isRhombus) {
            rows = countRows * 2 - 1;
            cols = countColumns * 2 - 1;
        } else {
            rows = countRows;
            cols = countColumns;
        }

        winCount = count2Win;

        if (rows < winCount && cols < winCount) {
            throw new IllegalArgumentException();
        }

        cells = new Cell[rows][cols];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }

        turn = Cell.X;

        countCells = 0;

        position = new PositionMNK();
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public MoveBoardResult makeMove(final Move move) {
        if (!position.isValid(move)) {
            return MoveBoardResult.LOSE;
        }

        switch (move.getMoveType()) {
            case GiveUp:
                return MoveBoardResult.LOSE;
            case Draw:
                return MoveBoardResult.DRAW_INVITE;
            default:
                break;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        countCells++;

        int cntDiag1 = getCntCells(move.getRow(), move.getColumn(), 1, 1, move.getValue()) +
                getCntCells(move.getRow() - 1, move.getColumn() - 1, -1, -1, move.getValue());
        int cntDiag2 = getCntCells(move.getRow(), move.getColumn(), 1, -1, move.getValue()) +
                getCntCells(move.getRow() - 1, move.getColumn() + 1, -1, 1, move.getValue());

        int cntRow = getCntCells(move.getRow(), move.getColumn(), 0, -1, move.getValue()) +
                getCntCells(move.getRow(), move.getColumn() + 1, 0, 1, move.getValue());

        int cntCol = getCntCells(move.getRow(), move.getColumn(), -1, 0, move.getValue()) +
                getCntCells(move.getRow() + 1, move.getColumn(), 1, 0, move.getValue());


        if (Math.max(Math.max(cntDiag1, cntDiag2), Math.max(cntCol, cntRow)) >= winCount) {
            return MoveBoardResult.WIN;
        }
        if (countCells >= rows * cols) {
            return MoveBoardResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return MoveBoardResult.UNKNOWN;
    }

    private int getCntCells(int row, int col, int stepRow, int stepCol, Cell value) {
        int cnt = 0;
        while (isValid(row, col) && cells[row][col] == value) {
            cnt++;
            row += stepRow;
            col += stepCol;
        }
        return cnt;
    }

    private boolean isValid(final int row, final int column) {
        boolean flag = row >= 0 && row < rows && column >= 0 && column < cols;
        if (!isRhombus) {
            return flag;
        }
        int colsHalf = cols / 2;
        int rowsHalf = rows / 2;
        int rowsPart = rowsHalf - Math.abs(row - rowsHalf);

        boolean flagRhombus = column >= colsHalf - rowsPart && column <= colsHalf + rowsPart;
        return flagRhombus && flag;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String borderHorizontalSign = "_";
        String borderVerticalSign = "|";

        String[] rowsStr = new String[rows];
        String[] colsStr = new String[cols];
        for (int i = 0; i < rows; i++) {
            rowsStr[i] = String.valueOf(i);
        }
        for (int i = 0; i < cols; i++) {
            colsStr[i] = String.valueOf(i);
        }
        String matrixBorderHorizontal = borderHorizontalSign.repeat(
                Math.max(0, cols * 2 + rowsStr[rowsStr.length - 1].length()));

        for (int i = 0; i < colsStr[colsStr.length - 1].length(); i++) {
            sb.append(" ".repeat(rowsStr[rowsStr.length - 1].length())).append(borderVerticalSign);
            for (int j = 0; j < cols; j++) {
                if (i >= colsStr[j].length()) {
                    sb.append(" ").append(borderVerticalSign);
                    continue;
                }
                sb.append(colsStr[j].charAt(i));
                sb.append(borderVerticalSign);
            }
            if (i + 1 < colsStr[colsStr.length - 1].length()) {
                sb.append(System.lineSeparator());
            }
        }
        sb.append(System.lineSeparator()).append(matrixBorderHorizontal);
        for (int r = 0; r < rows; r++) {
            sb.append(System.lineSeparator());
            sb.append(rowsStr[r]);
            sb.append(" ".repeat(rowsStr[rowsStr.length - 1].length() - rowsStr[r].length()))
                    .append(borderVerticalSign);
            for (int c = 0; c < cols; c++) {
                if (isValid(r, c)) {
                    sb.append(SYMBOLS.get(cells[r][c]));
                } else {
                    sb.append(' ');
                }
                sb.append(borderVerticalSign);
            }
            sb.append(System.lineSeparator());
            sb.append(matrixBorderHorizontal);
        }
        return sb.toString();
    }
}
