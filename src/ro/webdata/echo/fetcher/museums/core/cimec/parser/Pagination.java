package ro.webdata.echo.fetcher.museums.core.cimec.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ro.webdata.echo.commons.Const;

import java.util.Optional;

public class Pagination {
    public static double getNoPages(Document doc, String lang) {
        double totalEntries = Pagination.getTotalEntries(doc, lang);
        double pageEntries = Pagination.getPageEntries(doc, lang);
        return Math.ceil(totalEntries / pageEntries);
    }

    private static double getTotalEntries(Document doc, String lang) {
        Element counter = getCounterWrapper(doc, lang);
        String text = counter.text();
        int fromIndex = getFromEndIndex(text, lang);
        String totalEntries = text.substring(fromIndex)
                .replaceAll("\u00A0", "")
                .trim();
        return Double.parseDouble(totalEntries);
    }

    private static double getPageEntries(Document doc, String lang) {
        Element counter = getCounterWrapper(doc, lang);
        String text = counter.text();
        int dashIndex = text.indexOf("-") + 1;
        int fromIndex = getFromStartIndex(text, lang);
        String pageEntries = text.substring(dashIndex, fromIndex)
                .replaceAll("\u00A0", "")
                .trim();
        return Double.parseDouble(pageEntries);
    }

    private static Element getCounterWrapper(Document doc, String lang) {
        Optional<Element> result = doc.select("div.rezultate").stream().filter(
                element -> element.text().contains(getCounterStartText(lang))
        ).findFirst();
        return result.orElse(null);
    }

    private static String getCounterStartText(String lang) {
        switch (lang) {
            case Const.LANG_EN: return "Results";
            case Const.LANG_RO: return "Rezultate";
            default: return "";
        }
    }

    private static int getFromEndIndex(String text, String lang) {
        switch (lang) {
            case Const.LANG_EN: return text.indexOf("from") + 4;
            case Const.LANG_RO: return text.indexOf("din") + 3;
            default: return 0;
        }
    }

    private static int getFromStartIndex(String text, String lang) {
        switch (lang) {
            case Const.LANG_EN: return text.indexOf("from");
            case Const.LANG_RO: return text.indexOf("din");
            default: return 0;
        }
    }
}
