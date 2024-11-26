package game;

import java.util.*;

public class SwissSystem {
    private static class PlayerInfo implements Comparable<PlayerInfo> {
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

    public SwissSystem(final List<Player> players) {
        this.players = players;
    }

    private List<PlayerInfo> groupPlay(List<PlayerInfo> scoreGroup, List<Set<PlayerInfo>> matchesPlayed,
                                       List<PlayerInfo> playerInfos) {
        Random rnd = new Random();
        boolean[] usedPlayer = new boolean[players.size()];
        int groupSz = scoreGroup.size();
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
                Player player1;
                Player player2;
                int playerNumber1;
                int playerNumber2;
                if (rnd.nextBoolean()) {
                    playerNumber1 = scoreGroup.get(i).number;
                    playerNumber2 = scoreGroup.get(j).number;
                } else {
                    playerNumber1 = scoreGroup.get(j).number;
                    playerNumber2 = scoreGroup.get(i).number;
                }
                player1 = players.get(playerNumber1 - 1);
                player2 = players.get(playerNumber2 - 1);

                System.out.println("New Game!");
                System.out.printf("X: %s (%d)    vs    O: %s (%d)%n", player1, playerNumber1,
                        player2, playerNumber2);
                Board board = new BoardMNK(10, 10, 3);
                GameResult result = new Game(false, player1, player2).play(board);
                if (result == GameResult.WinPlayer1) {
                    playerInfos.get(playerNumber1 - 1).addPoints(2);
                } else if (result == GameResult.WinPlayer2) {
                    playerInfos.get(playerNumber2 - 1).addPoints(2);
                } else {
                    playerInfos.get(playerNumber1 - 1).addPoints(1);
                    playerInfos.get(playerNumber2 - 1).addPoints(1);
                }
                usedPlayer[i] = true;
                usedPlayer[j] = true;
                matchesPlayed.get(scoreGroup.get(i).number - 1).add(scoreGroup.get(j));
                matchesPlayed.get(scoreGroup.get(j).number - 1).add(scoreGroup.get(i));
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

    private void showTable(final List<PlayerInfo> playerInfos) {
        int placeNumber = 0;
        int prevScore = -1;
        for (PlayerInfo playerInfo : playerInfos) {
            if (playerInfo.getPoints() != prevScore) {
                placeNumber++;
            }
            prevScore = playerInfo.getPoints();
            System.out.printf("%d: %s (%d) | %d%n", placeNumber, players.get(playerInfo.number - 1).getName(),
                    playerInfo.number, playerInfo.points);
        }
    }

    public void start() {
        List<Set<PlayerInfo>> matchesPlayed = new ArrayList<>(players.size());
        List<PlayerInfo> playerInfos = new ArrayList<>(players.size());

        for (int i = 0; i < players.size(); i++) {
            playerInfos.add(new PlayerInfo(i + 1));
            matchesPlayed.add(new HashSet<>());
        }
        shuffleList(playerInfos);
// todo: Поправить для нечетной ситуации
        List<PlayerInfo> scoreGroup = new ArrayList<>();
        for (int tour = 0; tour < Math.round(Math.ceil(Math.log(players.size()))); tour++) {
            showTable(playerInfos);
            playerInfos.sort(null);
            int prevScore = playerInfos.getFirst().getPoints();
            for (PlayerInfo player : playerInfos) {
                if (player.getPoints() != prevScore) {
                    prevScore = player.getPoints();
                }
                scoreGroup.add(player);
                scoreGroup = groupPlay(scoreGroup, matchesPlayed, playerInfos);
            }
        }
        if (!scoreGroup.isEmpty()) {
            groupPlay(scoreGroup, matchesPlayed, playerInfos);
        }
        playerInfos.sort(null);
        showTable(playerInfos);
    }
}
