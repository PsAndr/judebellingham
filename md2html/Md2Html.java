package md2html;

import scanner.MyScanner;

import markup.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Md2Html {
    public enum TypeTextGroup {
        None,
        Paragraph,
        Header,
    }

    enum SplitSign {
        DoubleStar,
        Star,
        DoubleUnder,
        Under,
        DoubleHyphen,
        Apostrophe,
        LeftArrows,
        RightArrows,
        CloseBraces,
        OpenBraces,
    }

    record SplitSignsElem(int ind, SplitSign splitSign) {
    }

    private static int getSizeSplitSign(SplitSign splitSign) {
        return splitSignsValues.get(splitSign).length();
    }

    private static boolean isOpenSign(SplitSign splitSign) {
        return switch (splitSign) {
            case RightArrows, OpenBraces -> false;
            default -> true;
        };
    }

    private static boolean isPairSigns(SplitSign open, SplitSign close) {
        return switch (open) {
            case LeftArrows -> close == SplitSign.RightArrows;
            case CloseBraces -> close == SplitSign.OpenBraces;
            default -> open == close;
        };
    }

    private static final EnumMap<SplitSign, String> splitSignsValues = new EnumMap<>(SplitSign.class);
    static {
        splitSignsValues.putAll(Map.of(
                SplitSign.Star, "*",
                SplitSign.DoubleStar, "**",
                SplitSign.Under, "_",
                SplitSign.DoubleUnder, "__",
                SplitSign.DoubleHyphen, "--",
                SplitSign.Apostrophe, "`",
                SplitSign.LeftArrows, "<<",
                SplitSign.RightArrows, ">>",
                SplitSign.CloseBraces, "}}",
                SplitSign.OpenBraces, "{{"
        ));
    }

    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];

        List<HtmlAble> ans = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int levelHeader = 0;
        TypeTextGroup group = TypeTextGroup.None;

        try (MyScanner scanner = new MyScanner(new FileInputStream(inputFile))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.endsWith(System.lineSeparator())) {
                    line = line.substring(0, line.length() - System.lineSeparator().length());
                }
                if (line.isBlank()) {
                    dropNewElement(ans, sb, levelHeader, group);
                    group = TypeTextGroup.None;
                    levelHeader = 0;
                    continue;
                }
                switch (group) {
                    case Header:
                        sb.append(System.lineSeparator());
                        sb.append(line);
                        break;
                    case None:
                        if (line.startsWith("#")) {
                            int i = 0;
                            while (i < line.length() && line.charAt(i) == '#') {
                                sb.append('#');
                                i++;
                            }
                            if (i == line.length() || line.charAt(i) == ' ') {
                                levelHeader = sb.length();
                                sb.setLength(0);
                                group = TypeTextGroup.Header;
                                sb.append(line.substring(i + 1));
                                continue;
                            }
                        }
                        sb.setLength(0);
                        group = TypeTextGroup.Paragraph;
                    case Paragraph:
                        if (!sb.isEmpty()) {
                            sb.append(System.lineSeparator());
                        }
                        sb.append(line);
                        break;
                }
            }
        } catch (MyScanner.CanNotReadSourceStream ex) {
            System.err.println("Error while reading file: " + ex.getMessage());
        } catch (IOException io) {
            System.err.println("Error while opening file: " + io.getMessage());
        }

        dropNewElement(ans, sb, levelHeader, group);

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile),
                        StandardCharsets.UTF_8), 1024)) {
            for (HtmlAble s : ans) {
                sb.setLength(0);
                s.toHtml(sb);
                bw.write(sb.toString());
                bw.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            System.err.println("Error while writing output: " + ex.getMessage());
        }
    }


    private static void dropNewElement(List<HtmlAble> ans, StringBuilder sb, int levelHeader, TypeTextGroup group) {
        if (group != TypeTextGroup.None) {
            List<SplitSignsElem> splitSigns = new ArrayList<>();

            String paragraphText = sb.toString();

            boolean isIgnore = false;
            for (int i = 0; i < paragraphText.length(); i++) {
                char ch = paragraphText.charAt(i);
                if (!isIgnore) {
                    if (ch == '\\') {
                        isIgnore = true;
                        continue;
                    } else {
                        for (SplitSign splitSign : SplitSign.values()) {
                            String signs = splitSignsValues.get(splitSign);
                            boolean flag = paragraphText.length() - i >= signs.length();
                            if (flag) {
                                for (int j = 0; j < signs.length(); j++) {
                                    if (paragraphText.charAt(i + j) != signs.charAt(j)) {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            if (flag) {
                                splitSigns.add(new SplitSignsElem(i + signs.length() - 1, splitSign));
                                i += signs.length() - 1;
                                break;
                            }
                        }
                    }
                }
                isIgnore = false;
            }
            List<TextElement> paragraphElems = parseSplitSignsList(0, splitSigns.size(),
                    0, paragraphText.length(), splitSigns, paragraphText);
            // System.err.printf("%s%n", paragraphText);
            switch (group) {
                case Paragraph:
                    ans.add(new Paragraph(paragraphElems));
                    break;
                case Header:
                    ans.add(new Header(paragraphElems, levelHeader));
            }
        }
        sb.setLength(0);
    }

    private static List<TextElement> parseSplitSignsList(int l, int r, int ls, int rs,
                                                         List<SplitSignsElem> splitSigns,
                                                         String textParagraph) {
        if (l >= r) {
            // System.err.printf("All text parse: %s%n", textParagraph.substring(ls, rs));
            return List.of(new Text(textParagraph.substring(ls, rs)));
        }
        List<TextElement> paragraphElems = new ArrayList<>();
        // System.err.printf("%d %d %n", l, r);

        for (int i = l; i < r; i++) {
            boolean flag = false;
            int prevInd = ls;
            if (i > l) {
                prevInd = splitSigns.get(i - 1).ind + 1;
            }
            int ind = splitSigns.get(i).ind;
            ind -= getSizeSplitSign(splitSigns.get(i).splitSign) - 1;
            if (ind - prevInd > 0) {
                // System.err.printf("Before element parse: %s%n", textParagraph.substring(prevInd, ind));
                paragraphElems.add(new Text(textParagraph.substring(prevInd, ind)));
            }
            if (isOpenSign(splitSigns.get(i).splitSign)) {
                for (int j = i + 1; j < r; j++) {
                    if (isPairSigns(splitSigns.get(i).splitSign, splitSigns.get(j).splitSign)) {
                        int szSignEnd = getSizeSplitSign(splitSigns.get(j).splitSign);
                        List<TextElement> innerElements = parseSplitSignsList(i + 1, j,
                                splitSigns.get(i).ind + 1, splitSigns.get(j).ind - szSignEnd + 1,
                                splitSigns, textParagraph);

                        paragraphElems.add(switch (splitSigns.get(i).splitSign) {
                            case Apostrophe -> new Code(innerElements);
                            case Star, Under -> new Emphasis(innerElements);
                            case DoubleStar, DoubleUnder -> new Strong(innerElements);
                            case DoubleHyphen -> new Strikeout(innerElements);
                            case LeftArrows -> new Insert(innerElements);
                            case CloseBraces -> new Delete(innerElements);
                            default -> new Text("");
                        });
                        i = j;
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                paragraphElems.add(new Text(splitSignsValues.get(splitSigns.get(i).splitSign)));
            }
        }
        int ind = ls;
        if (r - 1 >= l) {
            ind = splitSigns.get(r - 1).ind + 1;
        }
        if (rs - ind > 0) {
            paragraphElems.add(new Text(textParagraph.substring(ind, rs)));
            // System.err.printf("End parse: %s%n", textParagraph.substring(ind, rs));
        }
        // System.err.printf("Ind + Next Ind: %d %d %n", ind, rs);
        return paragraphElems;
    }
}
