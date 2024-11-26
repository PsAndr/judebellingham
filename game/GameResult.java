package game;

public enum GameResult {
    WinPlayer1(1),
    WinPlayer2(2),
    Draw(0);

    public final int value;

    GameResult(final int value) {
        this.value = value;
    }
}
