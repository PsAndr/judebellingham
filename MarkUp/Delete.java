package markup;

import java.util.List;

public class Delete extends MarkupElement {
    public Delete(final List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "del";
    }

    @Override
    protected String getHtmlTag() {
        return "del";
    }

    @Override
    protected String getMarkdownSignsOpen() {
        return "}}";
    }

    @Override
    protected String getMarkdownSignsClose() {
        return "{{";
    }
}
