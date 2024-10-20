package markup;

import java.util.List;

public abstract class MarkUpBase extends Paragraph {
    public MarkUpBase(List<MarkDownAble> elements) {
        super(elements);
    }

    protected abstract String getSignsMarkDown();

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(getSignsMarkDown());
        super.toMarkdown(sb);
        sb.append(getSignsMarkDown());
    }
}
