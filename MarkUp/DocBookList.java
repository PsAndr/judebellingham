package markup;

import java.util.List;

public abstract class DocBookList extends DocBookWithElements<ListItem> implements GroupElement {
    public DocBookList(final List<ListItem> items) {
        super(items);
    }
}
