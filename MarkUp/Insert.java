package markup;

import java.util.List;

public class Insert extends MarkupElement {
    public Insert(final List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "ins";
    }

    @Override
    protected String getHtmlTag() {
        return "ins";
    }

    @Override
    protected String getMarkdownSignsOpen() {
        return "<<";
    }

    @Override
    protected String getMarkdownSignsClose() {
        return ">>";
    }
}
