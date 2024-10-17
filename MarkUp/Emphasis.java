package markup;

import java.util.List;

public class Emphasis extends Paragraph {
    public Emphasis(List<MarkDownAble> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append('*');
        super.toMarkdown(sb);
        sb.append('*');
    }
}
