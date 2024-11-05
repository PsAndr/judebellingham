package markup;

import java.util.List;
import java.util.Map;

public class Strikeout extends MarkupElement {
    public Strikeout(List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getHtmlTag() {
        return "s";
    }

    @Override
    protected String getMarkdownSigns() {
        return "~";
    }

    @Override
    protected Map<String, String> getDocBookParams() {
        return Map.of("role", "strikeout");
    }
}
