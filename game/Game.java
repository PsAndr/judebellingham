package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Game {
    private enum MoveResult {
        WinPlayer1(1),
        WinPlayer2(2),
        Draw(0),
        None(-1),
        DrawDenied(-2);

        public final int value;
        MoveResult(int value) {
            this.value = value;
        }
    }

    private static final int maxDrawRequests = 1;

    private final boolean log;
    private final Player player1, player2;

    public Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        this.player1 = player1;
        this.player2 = player2;
    }

    private GameResult convert2GameResult(MoveResult moveRes) {
        return switch (moveRes) {
            case WinPlayer1 -> GameResult.WinPlayer1;
            case WinPlayer2 -> GameResult.WinPlayer2;
            case Draw -> GameResult.Draw;
            default -> throw new IllegalStateException("Unexpected value: " + moveRes);
        };
    }

    public GameResult play(Board board) {
        while (true) {
            MoveResult result1 = getResultForPlayer(board, player1, player2, 1);
            if (result1 != MoveResult.None) {
                return convert2GameResult(result1);
            }

            MoveResult result2 = getResultForPlayer(board, player2, player1, 2);
            if (result2 != MoveResult.None) {
                return convert2GameResult(result2);
            }
        }
    }

    private MoveResult getResultForPlayer(final Board board, final Player player, final Player otherPlayer,
                                  final int no) {
        MoveResult result;
        int cntDrawRequests = 0;
        do {
            if (cntDrawRequests >= maxDrawRequests) {
                return no == 1 ? MoveResult.WinPlayer2 : MoveResult.WinPlayer1;
            }
            result = move(board, player, otherPlayer, no);
            cntDrawRequests++;
        } while (result == MoveResult.DrawDenied);
        return result;
    }

    private MoveResult move(final Board board, final Player player, final Player player2, final int no) {
        final Move move;
        try {
            move = player.move(board.getPosition(), board.getCell());
        } catch (final Exception ignore) {
            return no == 1 ? MoveResult.WinPlayer2 : MoveResult.WinPlayer1;
        }
        final MoveBoardResult result;
        try {
            result = board.makeMove(move);
        } catch (final Exception ignore) {
            return no == 1 ? MoveResult.WinPlayer2 : MoveResult.WinPlayer1;
        }
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == MoveBoardResult.WIN) {
            log("Player " + no + " won");
            return no == 1 ? MoveResult.WinPlayer1 : MoveResult.WinPlayer2;
        } else if (result == MoveBoardResult.LOSE) {
            log("Player " + no + " lose");
            return no == 1 ? MoveResult.WinPlayer2 : MoveResult.WinPlayer1;
        } else if (result == MoveBoardResult.DRAW) {
            log("Draw");
            return MoveResult.Draw;
        } else if (result == MoveBoardResult.DRAW_INVITE) {
            log("Draw invite");
            boolean isDraw;
            try {
                isDraw = player2.drawInvite();
            } catch (final Exception ignore) {
                isDraw = true;
            }
            if (isDraw) {
                log("Draw");
                return MoveResult.Draw;
            }
            log("Draw was denied");
            return MoveResult.DrawDenied;
        } else {
            return MoveResult.None;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
