package markup;

import java.util.List;

public class Paragraph extends DockBookMarkdownWithElems<TextElement> implements GroupElement, MarkdownAble {
    public Paragraph(final List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "para";
    }

    @Override
    protected String getHtmlTag() {
        return "p";
    }
}
