package markup;

import java.util.List;

public class Strikeout extends MarkUpBase {
    public Strikeout(List<MarkDownAble> elements) {
        super(elements);
    }

    @Override
    protected String getSignsMarkDown() {
        return "~";
    }
}
