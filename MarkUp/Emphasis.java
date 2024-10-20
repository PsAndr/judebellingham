package markup;

import java.util.List;

public class Emphasis extends MarkUpBase {
    public Emphasis(List<MarkDownAble> elements) {
        super(elements);
    }

    @Override
    protected String getSignsMarkDown() {
        return "*";
    }
}
