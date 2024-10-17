package markup;

import base.Selector;
import base.TestCounter;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class MarkupTest {
    public static final Selector SELECTOR = new Selector(MarkupTest.class)
            .variant("Base", MarkupTest.variant("Markdown", Map.of(
                    "&[", "", "&]", "",
                    "<", "", ">", ""
            )))
            .variant("DocBook", MarkupTest.variant("DocBook", Map.of(
                    "&[", "<para>", "&]", "</para>",
                    "*<", "<emphasis>", "*>", "</emphasis>",
                    "__<", "<emphasis role='bold'>", "__>", "</emphasis>",
                    "~<", "<emphasis role='strikeout'>", "~>", "</emphasis>"
            )))
            ;

    public static Consumer<TestCounter> variant(final String name, final Map<String, String> mapping) {
        return MarkupTester.variant(MarkupTest::test, name, mapping);
    }

    private MarkupTest() {
    }

    public static void test(final MarkupTester.Checker checker) {
        test(checker, new Paragraph(List.of(new Text("Hello"))), "Hello");
        test(checker, new Paragraph(List.of(new Emphasis(List.of(new Text("Hello"))))), "*<Hello*>");
        test(checker, new Paragraph(List.of(new Strong(List.of(new Text("Hello"))))), "__<Hello__>");
        test(checker, new Paragraph(List.of(new Strikeout(List.of(new Text("Hello"))))), "~<Hello~>");

        final Paragraph paragraph = new Paragraph(List.of(
                new Strong(List.of(
                        new Text("1"),
                        new Strikeout(List.of(
                                new Text("2"),
                                new Emphasis(List.of(
                                        new Text("3"),
                                        new Text("4")
                                )),
                                new Text("5")
                        )),
                        new Text("6")
                ))
        ));
        test(checker, paragraph, "__<1~<2*<34*>5~>6__>");
    }

    private static void test(final MarkupTester.Checker checker, final Paragraph paragraph, final String template) {
        checker.test(paragraph, String.format("&[%s&]", template));
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
