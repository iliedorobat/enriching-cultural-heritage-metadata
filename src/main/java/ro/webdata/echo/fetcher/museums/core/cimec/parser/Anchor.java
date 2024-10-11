package ro.webdata.echo.fetcher.museums.core.cimec.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.accessor.CimecAccessors;
import ro.webdata.echo.fetcher.museums.commons.CollectionsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ro.webdata.echo.commons.Const.*;

public class Anchor {
    public static List<String> getMuseumsLinks(String lang) {
        String baseUri = lang.equals(LANG_EN)
                ? MUSEUM_SEARCH_URI_EN
                : MUSEUM_SEARCH_URI_RO;
        Elements anchorList = getAllAnchors(baseUri, lang);
        return CollectionsUtils.mapAnchorsToLinks(Const.CIMEC_BASE_URI, anchorList);
    }

    /**
     * Get the links for all museums which contain links to detail pages
     * @param baseUri The base URI used to query museums<br/>
     *                (E.g.: "http://ghidulmuzeelor.cimec.ro/Filtru-Judete-EN.asp")
     * @param lang The language used
     * @return The list of links
     */
    private static Elements getAllAnchors(String baseUri, String lang) {
        ArrayList<Element> anchorList = new ArrayList<>();

        try {
            Document mainDoc = Jsoup.connect(baseUri).get();
            double pages = Pagination.getNoPages(mainDoc, lang);

            for (int i = 0; i < pages; i++) {
                int page = i + 1;
                Document doc = Jsoup.connect(baseUri + "?pageno=" + page).get();
                anchorList.addAll(
                        getAnchors(doc, "a.fontlink", lang)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Elements(anchorList);
    }

    /**
     * Get the links for all subordinated units of a museum
     * @param doc The museum page<br/>
     *            (E.g.: http://ghidulmuzeelor.cimec.ro/idEN.asp?k=1480&-Casa-Memoriala-Rosetti-Tescanu-George-Enescu-TESCANI-Bacau)
     * @param lang The target language
     * @return The list of links
     */
    public static List<String> getSubordinatesLinks(Document doc, String lang) {
        Elements anchorList = getAnchors(doc, "a", lang);
        String baseUri = CIMEC_BASE_URI + getMuseumPath(lang);
        return CollectionsUtils.mapAnchorsToLinks(baseUri, anchorList);
    }

    /**
     * Get the anchor tags which contains links to detail pages
     * @param doc The target document
     * @param anchorSelector The anchor selector (E.g.: "a"; "a.fontlink")
     * @param lang The language used
     * @return The list of links
     */
    private static Elements getAnchors(Document doc, String anchorSelector, String lang) {
        String dataTileValue = lang.equals(LANG_EN)
                ? CimecAccessors.EN_MUSEUM_NAME
                : CimecAccessors.RO_MUSEUM_NAME;

        Elements tableList = doc.select("table");

        if (tableList.size() > 0) {
            Element table = CollectionsUtils.filterListByAttr(tableList, "id", "myTable", true).get(0);

            Elements tdList = table.select("tbody > tr > td");
            Elements nameList = CollectionsUtils.filterListByAttr(tdList, "data-title", dataTileValue, true);

            Elements list = CollectionsUtils.mapListByAttr(nameList, anchorSelector);
            ArrayList<Element> anchorList = new ArrayList<>(list);

            return new Elements(anchorList);
        }

        return new Elements(new ArrayList<>());
    }

    private static String getMuseumPath(String lang) {
        switch (lang) {
            case LANG_EN: return "idEN" + File.EXTENSION_SEPARATOR + File.EXTENSION_ASP;
            case LANG_RO: return "id" + File.EXTENSION_SEPARATOR + File.EXTENSION_ASP;
            default: return "";
        }
    }
}
