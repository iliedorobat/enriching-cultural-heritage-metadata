package ro.webdata.echo.fetcher.museums.commons;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionsUtils {
    public static List<String> splitString(String value, String separator) {
        return Arrays.stream(
                        value.split(separator)
                )
                .map(
                        CollectionsUtils::formatUri
                )
                .filter(
                        item -> item.length() > 0
                )
                .collect(
                        Collectors.toList()
                );
    }

    public static Elements filterListByAttr(Elements list, String attrName, String attrValue, boolean equals) {
        Stream<Element> steam = list.stream().filter(
                element -> equals == element.attr(attrName).contains(attrValue)
        );
        return new Elements(
                steam.collect(
                        Collectors.toList()
                )
        );
    }

    public static Elements filterListByText(Elements list, String text, boolean equals) {
        Stream<Element> steam = list.stream().filter(
                element -> equals == element.text().contains(text)
        );
        return new Elements(
                steam.collect(
                        Collectors.toList()
                )
        );
    }

    public static Elements mapListByAttr(List<Element> list, String attrName) {
        Stream<Element> steam = list.stream().map(
                element -> element.select(attrName).get(0)
        );
        return new Elements(
                steam.collect(
                        Collectors.toList()
                )
        );
    }

    public static List<String> mapAnchorsToLinks(String uri, Elements anchorList) {
        return anchorList.stream()
                .map(
                        item -> uri + item.select("a")
                                .attr("href")
                                // There are some cases where the link contains carriage return. E.g.:
                                // iden.asp?k=1957&-Colectia-Publica-din-cadrul-Universitatii-„1-Decembrie
                                // 1918”-ALBA-IULIA-Alba
                                .replaceAll("\\s", "")
                                // Encode Grave Accent (`) to avoid creating an invalid link
                                .replaceAll("\u0060", "%60")
                )
                .collect(
                        Collectors.toList()
                );
    }

    private static String formatUri(String uri) {
        String output = uri;

        // E.g.: "www.muzeulnationalbratianu.ro"
        if (output.toLowerCase().startsWith("www")) {
            output = "https://" + output;
        }

        // E.g.: "http://www.manastireaviforata.ro//"
        if (uri.endsWith("//")) {
            output = uri.substring(0, uri.length() - 1);
        }

        return output.replace("\r\n", "").trim();
    }
}
