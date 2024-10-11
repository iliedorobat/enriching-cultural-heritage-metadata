package ro.webdata.echo.fetcher.museums.core.cimec.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.fetcher.museums.commons.CollectionsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gallery {
    // Extract the pictures links
    public static List<String> getPicturesLinks(Element content) {
        String cimecBaseUri = Const.CIMEC_BASE_URI.substring(0, Const.CIMEC_BASE_URI.length() - 1);
        Elements pictures = getPictures(content);

        return pictures
                .stream()
                .map(item -> cimecBaseUri + item.attr("href"))
                .collect(Collectors.toList());
    }

    // Get the pictures containers
    private static Elements getPictures(Element content) {
        ArrayList<Element> pictures = new ArrayList<>();
        Elements galleries = getGalleries(content);

        for (Element gallery : galleries) {
            Elements items = gallery.select("div.carousel-item > a");
            pictures.addAll(items);
        }

        return new Elements(pictures);
    }

    // Get the galleries containers
    private static Elements getGalleries(Element content) {
        if (content.attr("class").contains("row")) {
            Elements galleryWrapper = content.children().get(0).children();
            return CollectionsUtils.filterListByAttr(galleryWrapper, "id", "gallery", true);
        }

        return new Elements();
    }
}
