package markup;

import java.util.List;
import java.util.Map;

public abstract class DocBookWithElements<T extends DocBookAble> implements DocBookAble {
    protected final List<T> elements;

    public DocBookWithElements(final List<T> elements) {
        this.elements = elements;
    }

    protected abstract String getDocBookTag();

    protected Map<String, String> getDocBookParams() {
        return Map.of();
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        String tag = getDocBookTag();
        sb.append('<').append(tag);
        for (Map.Entry<String, String> param : getDocBookParams().entrySet()) {
            sb.append(" ").append(param.getKey()).append("='").append(param.getValue()).append('\'');
        }
        sb.append('>');
        for (final T elem : elements) {
            elem.toDocBook(sb);
        }
        sb.append("</").append(tag).append('>');
    }
}
