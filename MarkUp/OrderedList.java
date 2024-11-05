package markup;

import java.util.List;

public class OrderedList extends DocBookList {
    public OrderedList(List<ListItem> items) {
        super(items);
    }

    @Override
    protected String getDocBookTag() {
        return "orderedlist";
    }
}
