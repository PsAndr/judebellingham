import java.util.ArrayList;
import java.io.*;
import java.math.BigInteger;

public class Sum {
    public static void main(String[] args) {
        int sum = 0;
        int split_arg = 0;
        int flagSign = 1;

        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (Character.isWhitespace(ch)) {
                    if (split_arg != 0) {
                        sum += split_arg * flagSign;
                        split_arg = 0;
                        flagSign = 1;
                    }
                } else {
                    if (ch == '-') {
                        flagSign = -1;
                    } else if (ch >= '0' && ch <= '9') {
                        split_arg *= 10;
                        split_arg += ch - '0';
                    }
                }
            }
            if (split_arg != 0) {
                sum += split_arg * flagSign;
                split_arg = 0;
                flagSign = 1;
            }
        }
        System.out.println(sum);
    }
}