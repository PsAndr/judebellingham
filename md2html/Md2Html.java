package md2html;

import scanner.MyScanner;

import markup.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Md2Html {
    public enum TypeTextGroup {
        None,
        Paragraph,
        Header,
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
                // System.err.printf("%s ||| %s%n", line, group);
            }
        } catch (MyScanner.CanNotReadSourceStream ex) {
            System.err.println("Error while reading file: " + ex.getMessage());
        } catch (IOException io) {
            System.err.println("Error while opening file: " + io.getMessage());
        }

        dropNewElement(ans, sb, levelHeader, group);

        // System.err.printf("LOL: %d%n", ans.size());

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile),
                        StandardCharsets.UTF_8), 1024)) {
            for (HtmlAble s : ans) {
                sb.setLength(0);
                s.toHtml(sb);
                // System.err.println(sb);
                bw.write(sb.toString());
                bw.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            System.err.println("Error while writing output: " + ex.getMessage());
        }
    }

    enum SplitSign {
        Star,
        DoubleStar,
        Under,
        DoubleUnder,
        DoubleHyphen,
        Apostrophe,
    }

    record SplitSignsElem(int ind, SplitSign splitSign) {
    }

    private static void dropNewElement(List<HtmlAble> ans, StringBuilder sb, int levelHeader, TypeTextGroup group) {
        if (group != TypeTextGroup.None) {
            List<SplitSignsElem> splitSigns = new ArrayList<>();

            String paragraphText = sb.toString();
            // System.err.printf("Paragraph: %s%n", paragraphText);

            boolean isIgnore = false;
            for (int i = 0; i < paragraphText.length(); i++) {
                char ch = paragraphText.charAt(i);
                if (!isIgnore) {
                    switch (ch) {
                        case '\\':
                            isIgnore = true;
                            continue;
                        case '*':
                            if (i + 1 < paragraphText.length() && paragraphText.charAt(i + 1) == '*') {
                                splitSigns.add(new SplitSignsElem(i + 1, SplitSign.DoubleStar));
                                i++;
                            } else {
                                splitSigns.add(new SplitSignsElem(i, SplitSign.Star));
                            }
                            break;
                        case '_':
                            if (i + 1 < paragraphText.length() && paragraphText.charAt(i + 1) == '_') {
                                splitSigns.add(new SplitSignsElem(i + 1, SplitSign.DoubleUnder));
                                i++;
                            } else {
                                splitSigns.add(new SplitSignsElem(i, SplitSign.Under));
                            }
                            break;
                        case '-':
                            if (i + 1 < paragraphText.length() && paragraphText.charAt(i + 1) == '-') {
                                splitSigns.add(new SplitSignsElem(i + 1, SplitSign.DoubleHyphen));
                                i++;
                            }
                            break;
                        case '`':
                            splitSigns.add(new SplitSignsElem(i, SplitSign.Apostrophe));
                            break;
                        default:
                            break;
                    }
                }
                isIgnore = false;
            }
            List<TextElement> paragraphElems = parseSplitSignsList(0, splitSigns.size(),
                    0, paragraphText.length(), splitSigns, paragraphText);
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

    private static int getSizeSplitSign(SplitSign splitSign) {
        return switch (splitSign) {
            case Star, Under, Apostrophe -> 1;
            case DoubleStar, DoubleHyphen, DoubleUnder -> 2;
        };
    }

    private static List<TextElement> parseSplitSignsList(int l, int r, int ls, int rs,
                                                         List<SplitSignsElem> splitSigns,
                                                         String textParagraph) {
        if (l >= r) {
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
                paragraphElems.add(new Text(textParagraph.substring(prevInd, ind)));
            }
            for (int j = i + 1; j < r; j++) {
                if (splitSigns.get(i).splitSign == splitSigns.get(j).splitSign) {
                    int szSignEnd = getSizeSplitSign(splitSigns.get(j).splitSign);
                    List<TextElement> innerElements = parseSplitSignsList(i + 1, j,
                            splitSigns.get(i).ind + 1, splitSigns.get(j).ind - szSignEnd + 1,
                            splitSigns, textParagraph);

                    paragraphElems.add(switch (splitSigns.get(i).splitSign) {
                        case Apostrophe -> new Code(innerElements);
                        case Star, Under -> new Emphasis(innerElements);
                        case DoubleStar, DoubleUnder -> new Strong(innerElements);
                        case DoubleHyphen -> new Strikeout(innerElements);
                    });
                    i = j;
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                paragraphElems.add(new Text(String.valueOf(textParagraph.charAt(splitSigns.get(i).ind))));
            }
        }
        int ind = ls;
        if (r - 1 >= l) {
            ind = splitSigns.get(r - 1).ind + 1;
        }
        if (rs - ind > 0) {
            paragraphElems.add(new Text(textParagraph.substring(ind, rs)));
        }
        // System.err.printf("Ind + Next Ind: %d %d %n", ind, rs);
        return paragraphElems;
    }
}
