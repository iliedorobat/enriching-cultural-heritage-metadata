package ro.webdata.echo.fetcher.museums.commons;

import org.apache.commons.lang3.StringUtils;
import org.geonames.*;

import java.util.HashMap;
import java.util.List;

/**
 * @deprecated not used anymore
 */
public class GpsUtils {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    // username = "ilie"
    // countryCode = "RO"
    // language = "ro"
    // feature = FeatureClass.P
    public static HashMap<String, Double> getCoordinates(
            String username,
            String locationName,
            String countryCode,
            String language,
            FeatureClass feature,
            String postalCode
    ) {
        HashMap<String, Double> coordinates = new HashMap<>();
        String name = StringUtils.stripAccents(
                locationName.toLowerCase()
        );
        WebService.setUserName(username);

        try {
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setCountryCode(countryCode);
            searchCriteria.setLanguage(language);
            searchCriteria.setFeatureClass(feature);
            searchCriteria.setName(name);

            ToponymSearchResult searchResult = WebService.search(searchCriteria);
            List<Toponym> toponyms = searchResult.getToponyms();

            for (Toponym toponym: toponyms) {
                String toponymName = StringUtils.strip(
                        toponym.getName().toLowerCase()
                );

                // Check if the current toponym has the same name with the provided one
                if (toponymName.equals(name)) {
                    if (toponyms.size() == 1) {
                        coordinates.put(LATITUDE, toponym.getLatitude());
                        coordinates.put(LONGITUDE, toponym.getLongitude());
                    }
                    // Search by the postal code if there is more than one entry
                    else {
                        List<PostalCode> postalCodes = WebService.postalCodeSearch(postalCode, name, countryCode);
                        switch (postalCodes.size()) {
                            case 0:
                                System.err.println(
                                        "No entries were identified!" +
                                        "\n\tlocationName = \"" + locationName + "\"" +
                                        "\n\tpostalCode = \"" + postalCode + "\""
                                );
                                break;
                            case 1:
                                coordinates.put(LATITUDE, postalCodes.get(0).getLatitude());
                                coordinates.put(LONGITUDE, postalCodes.get(0).getLongitude());
                                break;
                            default:
                                System.err.println(
                                        "There were identified too many entries!" +
                                        "\n\tlocationName = \"" + locationName + "\"" +
                                        "\n\tpostalCode = \"" + postalCode + "\""
                                );
                                break;
                        }
                    }
                }
            }
        } catch (InvalidParameterException e) {
            // TODO: setCountryCode error
        } catch (Exception e) {
            // TODO: WebService.search(searchCriteria) error
        }

        return coordinates;
    }
}
