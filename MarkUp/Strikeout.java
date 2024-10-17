package markup;

import java.util.List;

public class Strikeout extends Paragraph {
    public Strikeout(List<MarkDownAble> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append('~');
        super.toMarkdown(sb);
        sb.append('~');
    }
}
