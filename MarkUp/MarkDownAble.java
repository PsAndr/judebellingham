package markup;

public interface MarkdownAble extends HtmlAble {
    void toMarkdown(final StringBuilder sb);
}
