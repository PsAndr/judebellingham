package markup;

import java.util.List;
import java.util.Map;

public class Strong extends MarkupElement {
    public Strong(List<TextElement> elements) {
        super(elements);
    }

    @Override
    protected String getMarkdownSigns() {
        return "__";
    }

    @Override
    protected String getHtmlTag() {
        return "strong";
    }

    @Override
    protected Map<String, String> getDocBookParams() {
        return Map.of("role", "bold");
    }
}
