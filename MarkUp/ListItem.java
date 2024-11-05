package markup;

import java.util.List;

public class ListItem extends DocBookWithElements<GroupElement> {
    public ListItem(final List<GroupElement> elements) {
        super(elements);
    }

    @Override
    protected String getDocBookTag() {
        return "listitem";
    }
}
