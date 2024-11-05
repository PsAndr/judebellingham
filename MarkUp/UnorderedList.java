package markup;

import java.util.List;

public class UnorderedList extends DocBookList {
    public UnorderedList(final List<ListItem> items) {
        super(items);
    }

    @Override
    protected String getDocBookTag() {
        return "itemizedlist";
    }
}
