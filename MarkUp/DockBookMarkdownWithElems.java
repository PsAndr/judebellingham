package markup;

import java.util.List;

public abstract class DockBookMarkdownWithElems<T extends MarkdownAble & DocBookAble>
        extends DocBookWithElements<T> implements MarkdownAble {
    public DockBookMarkdownWithElems(List<T> elements) {
        super(elements);
    }

    protected String getMarkdownSignsOpen() {
        return "";
    }

    protected String getMarkdownSignsClose() {
        return getMarkdownSignsOpen();
    }

    protected abstract String getHtmlTag();

    @Override
    public void toMarkdown(final StringBuilder sb) {
        sb.append(getMarkdownSignsOpen());
        for (final T element: elements) {
            element.toMarkdown(sb);
        }
        sb.append(getMarkdownSignsClose());
    }

    @Override
    public void toHtml(final StringBuilder sb) {
        sb.append('<').append(getHtmlTag()).append('>');
        for (final T element: elements) {
            element.toHtml(sb);
        }
        sb.append("</").append(getHtmlTag()).append('>');
    }
}
