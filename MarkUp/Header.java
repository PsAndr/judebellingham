package markup;

import java.util.List;

public class Header extends Paragraph {
    private final int level;

    public Header(final List<TextElement> elements, final int level) {
        super(elements);
        this.level = level;
    }

    @Override
    protected String getHtmlTag() {
        return "h" + level;
    }
}
