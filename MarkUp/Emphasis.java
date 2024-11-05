package markup;

import java.util.List;

public class Emphasis extends MarkupElement {
    public Emphasis(List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getHtmlTag() {
        return "em";
    }

    @Override
    protected String getMarkdownSigns() {
        return "*";
    }
}
