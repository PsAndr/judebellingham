package markup;

import java.util.List;

public class Strong extends MarkUpBase {
    public Strong(List<MarkDownAble> elements) {
        super(elements);
    }

    @Override
    protected String getSignsMarkDown() {
        return "__";
    }
}
