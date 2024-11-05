package markup;

import java.util.List;

public class Code extends MarkupElement {
    public Code(final List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "code";
    }

    @Override
    protected String getHtmlTag() {
        return "code";
    }

    @Override
    protected String getMarkdownSigns() {
        return "`";
    }
}
