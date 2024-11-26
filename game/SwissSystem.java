package game;

import java.io.PrintStream;
import java.util.*;

public class SwissSystem {
    private class PlayerInfo implements Comparable<PlayerInfo> {
        public final int number;
        private int points;

        public PlayerInfo(int number) {
            this.number = number;
            points = 0;
        }

        public void addPoints(int points) {
            this.points += points;
        }

        public int getPoints() {
            return points;
        }

        public void resetPoints() {
            points = 0;
        }

        public String getName() {
            return getPlayer().getName();
        }

        public Player getPlayer() {
            return players.get(number - 1);
        }

        @Override
        public String toString() {
            return String.format("Player no.%d (points: %d)", number, points);
        }

        @Override
        public int compareTo(PlayerInfo o) {
            return o.points - points;
        }

        @Override
        public int hashCode() {
            return number;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PlayerInfo) {
                return number == ((PlayerInfo) obj).number;
            }
            return false;
        }
    }

    private record MatchInfo(int number, GameResult result, PlayerInfo player1, PlayerInfo player2) {
        @Override
        public String toString() {
            return String.format("%d. %s (%d)    vs    %s (%d)  |  %s", number, player1.getPlayer(),
                    player1.number, player2.getPlayer(), player2.number, result);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MatchInfo) {
                return number == ((MatchInfo) obj).number && result == ((MatchInfo) obj).result
                        && player1 == ((MatchInfo) obj).player1 && player2 == ((MatchInfo) obj).player2;
            }
            return false;
        }
    }

    private static void shuffleList(List<PlayerInfo> ls) {
        Random rnd = new Random();
        for (int i = ls.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            PlayerInfo a = ls.get(index);
            ls.set(index, ls.get(i));
            ls.set(i, a);
        }
    }

    private final List<Player> players;
    private final List<PlayerInfo> playerInfos;
    private final List<MatchInfo> matchesInfos;

    private final boolean log;

    public SwissSystem(final List<Player> players, final boolean log) {
        if (players.isEmpty()) {
            throw new IllegalArgumentException("Player list is empty");
        }
        this.log = log;
        this.players = players;
        playerInfos = new ArrayList<>(players.size());
        matchesInfos = new ArrayList<>(players.size());
        for (int i = 0; i < players.size(); i++) {
            playerInfos.add(new PlayerInfo(i + 1));
        }
    }

    public SwissSystem(final List<Player> players) {
        this(players, true);
    }

    private List<PlayerInfo> groupPlay(List<PlayerInfo> scoreGroup, List<Set<PlayerInfo>> matchesPlayed) {
        Random rnd = new Random();
        int groupSz = scoreGroup.size();
        boolean[] usedPlayer = new boolean[groupSz];
        for (int i = 0; i < groupSz; i++) {
            for (int j = 0; j < groupSz; j++) {
                if (i == j) {
                    continue;
                }
                if (usedPlayer[i] || usedPlayer[j]) {
                    continue;
                }
                if (matchesPlayed.get(scoreGroup.get(i).number - 1).contains(scoreGroup.get(j))) {
                    continue;
                }
                PlayerInfo player1;
                PlayerInfo player2;
                if (rnd.nextBoolean()) {
                    player1 = scoreGroup.get(i);
                    player2 = scoreGroup.get(j);
                } else {
                    player1 = scoreGroup.get(j);
                    player2 = scoreGroup.get(i);
                }

                log("New Game!");
                log(String.format("X: %s (%d)    vs    O: %s (%d)%n", player1.getPlayer(), player1.number,
                        player2.getPlayer(), player2.number));
                Board board = new BoardMNK(10, 10, 3);
                GameResult result = new Game(false, player1.getPlayer(), player2.getPlayer()).play(board);
                MatchInfo matchInfo = new MatchInfo(matchesInfos.size() + 1, result, player1, player2);
                matchesInfos.add(matchInfo);

                if (result == GameResult.WinPlayer1) {
                    player1.addPoints(2);
                } else if (result == GameResult.WinPlayer2) {
                    player2.addPoints(2);
                } else {
                    player1.addPoints(1);
                    player2.addPoints(1);
                }
                usedPlayer[i] = true;
                usedPlayer[j] = true;
                matchesPlayed.get(player1.number - 1).add(player2);
                matchesPlayed.get(player2.number - 1).add(player1);
                log(String.format("Last match: %s%n", matchInfo));
                break;
            }
        }
        List<PlayerInfo> newScoreGroup = new ArrayList<>();
        for (int i = 0; i < groupSz; i++) {
            if (!usedPlayer[i]) {
                newScoreGroup.add(scoreGroup.get(i));
            }
        }
        return newScoreGroup;
    }

    public void showTable(final PrintStream out) {
        int placeNumber = 0;
        int prevScore = -1;
        for (PlayerInfo playerInfo : playerInfos) {
            if (playerInfo.getPoints() != prevScore) {
                placeNumber++;
            }
            prevScore = playerInfo.getPoints();
            out.printf("%d: %s (%d) | %d%n", placeNumber, players.get(playerInfo.number - 1).getName(),
                    playerInfo.number, playerInfo.points);
        }
    }

    public void showLastMatches(final PrintStream out, final int count) {
        int startIndex = Math.max(matchesInfos.size() - count, 0);
        for (int i = startIndex; i < matchesInfos.size(); i++) {
            out.println(matchesInfos.get(i).toString());
        }
    }

    public void showLastMatches(final int count) {
        showLastMatches(System.out, count);
    }

    public void showLastMatches(final PrintStream out) {
        showLastMatches(out, matchesInfos.size());
    }

    public void showLastMatches() {
        showLastMatches(matchesInfos.size());
    }

    public void showTable() {
        showTable(System.out);
    }

    public void start() {
        boolean[] isSkipped = new boolean[players.size()];

        List<Set<PlayerInfo>> matchesPlayed = new ArrayList<>(players.size());
        for (int i = 0; i < players.size(); i++) {
            playerInfos.get(i).resetPoints();
            matchesPlayed.add(new HashSet<>());
        }
        shuffleList(playerInfos);

        for (int tour = 0; tour < Math.round(Math.ceil(Math.log(players.size()))); tour++) {
            List<PlayerInfo> scoreGroup = new ArrayList<>();
            playerInfos.sort(null);
            log("Table now:");
            if (log) {
                showTable();
            }
            int skipPlayerNumber = -1;
            if (players.size() % 2 == 1) {
                for (int i = playerInfos.size() - 1; i >= 0; i--) {
                    if (isSkipped[playerInfos.get(i).number - 1]) {
                        continue;
                    }
                    skipPlayerNumber = playerInfos.get(i).number;
                    playerInfos.get(i).addPoints(2);
                    break;
                }
                isSkipped[skipPlayerNumber - 1] = true;
            }
            int prevScore = playerInfos.getFirst().getPoints();
            for (PlayerInfo player : playerInfos) {
                if (player.number == skipPlayerNumber) {
                    continue;
                }
                if (player.getPoints() != prevScore) {
                    scoreGroup = groupPlay(scoreGroup, matchesPlayed);
                }
                prevScore = player.getPoints();
                scoreGroup.add(player);
            }
            if (!scoreGroup.isEmpty()) {
                groupPlay(scoreGroup, matchesPlayed);
            }
        }
        playerInfos.sort(null);
        log("Final table: ");
        if (log) {
            showTable();
        }
        log("All matches: ");
        if (log) {
            showLastMatches();
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
