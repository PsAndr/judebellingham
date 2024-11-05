package markup;

public abstract class BaseText implements DocBookAble, MarkdownAble {
    protected final String text;
    public BaseText(final String text) {
        this.text = text;
    }

    @Override
    public void toHtml(final StringBuilder sb) {
        boolean flagBackSlash = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '\\':
                    if (flagBackSlash) {
                        sb.append(c);
                    } else {
                        flagBackSlash = true;
                        continue;
                    }
                    break;
                default:
                    sb.append(c);
            }
            flagBackSlash = false;
        }
    }

    @Override
    public void toDocBook(final StringBuilder sb) {
        sb.append(text);
    }

    @Override
    public void toMarkdown(final StringBuilder sb) {
        sb.append(text);
    }
}
