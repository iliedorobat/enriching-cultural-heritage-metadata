package ro.webdata.echo.translator.edm.approach.event.lido.commons;

import org.apache.jena.rdf.model.Literal;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.translator.commons.SyncHttpClient;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConceptService {
    private static final char[] DELIMITERS = new char[] {' ', '_'};
    private static final String NS = "https://dbpedia.org/page/";
    private static final HashMap<String, String> conceptsMap = new HashMap<>();

    public static void add(Literal literal) {
        String concept = literal.getString();
        String lang = literal.getLanguage();
        boolean isDBpediaConcept = lang != null && lang.equals("en");

        if (isDBpediaConcept) {
            String camelCaseConcept = Text.toCamelCase(concept, true, DELIMITERS);

            if (conceptsMap.get(camelCaseConcept) == null) {
                String dbpediaUri = prepareDBpediaUri(concept, false);
                String encodedDBpediaUri = prepareDBpediaUri(concept, true);
                HttpResponse<String> response = SyncHttpClient.callApi(encodedDBpediaUri);
                int statusCode = response != null
                        ? response.statusCode()
                        : -1;

                switch (statusCode) {
                    case HttpURLConnection.HTTP_OK:
                        // E.g.: "https://dbpedia.org/page/Creative_Commons_license" (200)
                        conceptsMap.put(camelCaseConcept, dbpediaUri);
                        break;
                    case HttpURLConnection.HTTP_MOVED_PERM:
                    case HttpURLConnection.HTTP_MOVED_TEMP:
                    case HttpURLConnection.HTTP_SEE_OTHER:
                        // E.g.: "https://dbpedia.org/page/CC0" (301)
                        // E.g.: "https://dbpedia.org/resource/CC0" (303)
                        HttpHeaders headers = response.headers();
                        Optional<String> location = headers.firstValue("location");
                        location.ifPresent(
                                newLink -> conceptsMap.put(
                                        camelCaseConcept,
                                        newLink
                                                .replaceAll("http://dbpedia.org/resource/", "http://dbpedia.org/page/")
                                                .replaceAll("https://dbpedia.org/resource/", "https://dbpedia.org/page/")
                                )
                        );
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static boolean exists(String concept) {
        return conceptsMap.get(concept) != null;
    }

    public static HashMap<String, String> get() {
        return conceptsMap;
    }

    public static String getConceptUri(String concept) {
        String camelCaseConcept = Text.toCamelCase(concept, true, DELIMITERS);
        return conceptsMap.get(camelCaseConcept);
    }

    private static String prepareDBpediaUri(String concept, boolean encode) {
        String relativeUri = prepareRelativeDBpediaUri(concept);

        if (encode) {
            return NS + URLEncoder.encode(relativeUri, StandardCharsets.UTF_8);
        }

        return NS + relativeUri;
    }

    private static String prepareRelativeDBpediaUri(String concept) {
        String[] terms = concept.split("[\\s_]");
        List<String> arrayList = Arrays.stream(terms)
                .map(term -> Text.toCamelCase(term, true, DELIMITERS))
                .collect(Collectors.toList());

        return String.join("_", arrayList);
    }
}
