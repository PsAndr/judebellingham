package game;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;
    private final String name;

    public HumanPlayer(final String name, final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
        this.name = name;
    }

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this("Human", out, in);
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    public HumanPlayer(String name) {
        this(name, System.out, new Scanner(System.in));
    }

    private void clearLineInput() {
        try {
            in.nextLine();
        } catch (Exception ignored) {
        }
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println(cell + "'s move");

            loopChooseTypeMove:
            while (true) {
                out.println("What do you want to do?");
                out.println("1. Make move");
                out.println("2. Draw");
                out.println("3. Give up");
                out.println("4. Show position");
                out.print("Enter number: ");
                try {
                    int number = in.nextInt();
                    switch (number) {
                        case 1:
                            break loopChooseTypeMove;
                        case 2:
                            return Move.DRAW;
                        case 3:
                            return Move.GIVE_UP;
                        case 4:
                            out.println("Position");
                            out.println(position);
                            break;
                        default:
                            out.println("Invalid number");
                            break;
                    }
                } catch (Exception e) {
                    out.println("Invalid input");
                } finally {
                    clearLineInput();
                }
            }
            out.print("Enter row and column [r c]: ");
            final Move move;
            try {
                move = new Move(in.nextInt(), in.nextInt(), cell);
                in.nextLine();
                if (position.isValid(move)) {
                    return move;
                }
            } catch (Exception ex) {
                out.println("Invalid input (can`t parse)");
                clearLineInput();
                continue;
            }
            out.println("Move " + move + " is invalid");
        }
    }

    @Override
    public boolean drawInvite() {
        out.println("Do you wanna draw? [Y/n]");
        return in.next().toLowerCase().startsWith("y");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Player %s", name);
    }
}
