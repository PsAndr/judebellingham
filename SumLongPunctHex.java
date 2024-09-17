public class SumLongPunctHex {
    private static long parse(String s) {
        if (s.toLowerCase().startsWith("0x")) {
            return Long.parseUnsignedLong(s.substring(2), 16);
        } else {
            return Long.parseLong(s);
        }
    }

    public static void main(String[] args) {
        long sum = 0;
        int indexLastEnd = -1;
        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                int typeCh = Character.getType(ch);
                if (Character.isWhitespace(ch) || Character.START_PUNCTUATION == typeCh ||
                        Character.END_PUNCTUATION == typeCh) {
                    if (i - indexLastEnd > 1) {
                        sum += parse(arg.substring(indexLastEnd + 1, i));
                    }
                    indexLastEnd = i;
                }
            }
            if (arg.length() - indexLastEnd > 1) {
                sum += parse(arg.substring(indexLastEnd + 1));
            }
        }
        System.out.println(sum);
    }
}
