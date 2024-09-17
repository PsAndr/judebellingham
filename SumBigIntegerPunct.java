import java.math.BigInteger;

public class SumBigIntegerPunct {
    public static void main(String[] args) {
        BigInteger sum = BigInteger.ZERO;
        int index_last_end = -1;
        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (ch != '-' && !Character.isDigit(ch)) {
                    if (i - index_last_end > 1) {
                        sum = sum.add(new BigInteger(arg.substring(index_last_end + 1, i)));
                    }
                    index_last_end = i;
                }
            }
            if (arg.length() - index_last_end > 1) {
                sum = sum.add(new BigInteger(arg.substring(index_last_end + 1)));
            }
        }
        System.out.println(sum);
    }
}
