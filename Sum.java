import java.util.ArrayList;
import java.io.*;
import java.math.BigInteger;

public class Sum {
    public static void main(String[] args) {
        int sum = 0;
        StringBuilder split_arg = new StringBuilder();

        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (Character.isWhitespace(ch)) {
                    if (split_arg.length() != 0) {
                        sum += Integer.valueOf(split_arg.toString());
                        split_arg.setLength(0);
                    }
                } else {
                    split_arg.append(ch);
                }
            }
            if (split_arg.length() != 0) {
                sum += Integer.valueOf(split_arg.toString());
                split_arg.setLength(0);
            }
        }
        System.out.println(sum);
    }
}