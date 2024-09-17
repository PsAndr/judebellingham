public class Sum {
    public static void main(String[] args) {
        int sum = 0;
        int index_last_end = -1;
        for (String arg : args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (Character.isWhitespace(ch)) {
                    if (i - index_last_end > 1) {
                        sum += Integer.parseInt(arg.substring(index_last_end + 1, i));
                    }
                    index_last_end = i;
                }
            }
            if (arg.length() - index_last_end > 1) {
                sum += Integer.parseInt(arg.substring(index_last_end + 1));
            }
        }
        System.out.println(sum);
    }
}