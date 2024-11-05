package markup;

import java.util.List;

public abstract class MarkupElement extends DockBookMarkdownWithElems<TextElement> implements TextElement {
    public MarkupElement(final List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "emphasis";
    }
}
