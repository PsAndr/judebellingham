import java.util.ArrayList;
import java.io.*;
import java.math.BigInteger;

public class Sum {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();

        for (String arg : args) {
            ArrayList<StringBuilder> split_arg = new ArrayList<StringBuilder>();
            split_arg.add(new StringBuilder());

            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (Character.isWhitespace(ch)) {
                    if (split_arg.get(split_arg.size() - 1).length() != 0) {
                        split_arg.add(new StringBuilder());
                    }
                } else {
                    split_arg.get(split_arg.size() - 1).append(ch);
                }
            }

            for (var el : split_arg) {
                String str_el = el.toString();
                if (el.length() == 0) {
                    continue;
                }
                list.add(str_el);
            }
        }

        int ans = 0;
        for (var el : list) {
                var elInt = Integer.valueOf(el);
                ans += elInt;
        }
        System.out.println(ans);
    }
}