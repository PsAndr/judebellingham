package markup;

import java.util.List;

public class Paragraph implements MarkDownAble {
    private final List<MarkDownAble> elements;

    public Paragraph(List<MarkDownAble> elements) {
        this.elements = elements;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        for (MarkDownAble elem : elements) {
            elem.toMarkdown(sb);
        }
    }
}
