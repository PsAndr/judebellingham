package expression;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static void skipLine() {
        try {
            scanner.nextLine();
        } catch (RuntimeException ignored) {
        }
    }

    private static int intInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (RuntimeException ignored) {
                skipLine();
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        Expression expression = new Add(new Subtract(new Multiply(new Variable("x"), new Variable("x")),
                new Multiply(new Const(2), new Variable("x"))), new Const(1));
        while (true) {
            System.out.println("Do you want calculate: " + expression.toMiniString() + "?");
            System.out.println("1 - yes, 0 - no");
            int choice = intInput("Please enter your choice: ");
            if (choice == 1) {
                int x = intInput("Enter x: ");
                System.out.println("Answer: " + expression.evaluate(x));
            } else if (choice == 0) {
                System.out.println("Bye!");
                break;
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}
