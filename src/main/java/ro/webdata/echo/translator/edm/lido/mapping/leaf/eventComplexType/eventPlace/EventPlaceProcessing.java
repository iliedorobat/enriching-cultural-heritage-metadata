package ro.webdata.echo.translator.edm.lido.mapping.leaf.eventComplexType.eventPlace;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.translator.commons.PropertyUtils;
import ro.webdata.echo.translator.edm.lido.commons.PlaceMapUtils;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_PLACE;
import static ro.webdata.echo.translator.commons.MuseumUtils.PLACE_NAME_SEPARATOR;
import static ro.webdata.echo.translator.commons.MuseumUtils.sanitizePlaceName;

public class EventPlaceProcessing {
    /**
     * Generate the list of Event Place for all places
     * @param model The RDF graph
     * @param eventPlaceList The LIDO Event Place list
     * @return The RDF Event Place list
     */
    public static ArrayList<Resource> generateEventPlaceList(
            Model model, ArrayList<EventPlace> eventPlaceList
    ) {
        ArrayList<Resource> placeList = new ArrayList<>();

        for (EventPlace eventPlace : eventPlaceList) {
            placeList.addAll(
                    generateEventPlace(model, eventPlace.getPlace())
            );
        }

        return placeList;
    }

    /**
     * Generate the deep Event Place list for a specified place<br/>
     * This list includes all the political places from which the place belongs to<br/>
     * E.g.: a city belongs to a county, which belongs to a region etc.
     * @param model The RDF graph
     * @param place The place
     * @return The list with Event Places
     */
    private static ArrayList<Resource> generateEventPlace(Model model, Place place) {
        ArrayList<Resource> allPlaceList = new ArrayList<>();
        ArrayList<Resource> placeList = new ArrayList<>();
        LinkedHashMap<String, HashMap<String, Object>> placeMapList = PlaceMapUtils.getPlaceMap(place);

        Resource resource = null;
        for (Map.Entry<String, HashMap<String, Object>> placeMap : placeMapList.entrySet()) {
            Resource placeResource = generateEventPlace(model, placeMap);
            resource = placeResource;
            allPlaceList.add(placeResource);
        }

        if (resource != null) {
            placeList.add(resource);
        }

        if (allPlaceList.size() > 0) {
            addIsPartOf(allPlaceList);
            addHasPart(allPlaceList);
        }

        return placeList;
    }

    // TODO: owl:sameAs => use geo names (https://www.geonames.org/search.html?q=&country=RO)
    //  translate the country name: https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application
    private static Resource generateEventPlace(Model model, Map.Entry<String, HashMap<String, Object>> placeMap) {
        HashMap<String, String> nameMap = (HashMap<String, String>) placeMap.getValue().get(PlaceMapUtils.NAME_PROP);
        String relativeUri = placeMap.getValue().get(PlaceMapUtils.URI_PROP).toString();
        String placeUri = URIUtils.prepareUri(NS_REPO_RESOURCE_PLACE, relativeUri);
        String placeType = placeMap.getKey();

        Resource placeResource = model.createResource(placeUri);

        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            String lang = entry.getKey();
            String name = entry.getValue();
            String sanitizedName = sanitizePlaceName(name, PLACE_NAME_SEPARATOR);

            addLabelProperty(placeResource, sanitizedName, lang);
            addLabelProperty(placeResource, name, lang);
        }

        if (placeType != null) {
            Literal literal = model.createLiteral(placeType, Const.LANG_EN);
            Property property = PropertyUtils.createSubProperty(model, SKOS.note, "politicalEntity", false);
            placeResource.addProperty(property, literal);
        }

//        // TODO: use owlSameAs to link the place to DBpedia
//        String dbpediaLink = ResourceUtils.generateDBPediaURI(placeName);
//        try { TimeUnit.SECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
//        if (HttpGet.isValidDBPedia(placeName)) {
//            placeResource.addProperty(OWL2.sameAs, model.createResource(dbpediaLink));
//        }

        return placeResource;
    }

    private static void addLabelProperty(Resource placeResource, String name, String language) {
        // Add "skos:prefLabel" property for the input language only if it has not already been added
        if (placeResource.getProperty(SKOS.prefLabel, language) == null) {
            placeResource.addProperty(SKOS.prefLabel, name, language);
        } else {
            placeResource.addProperty(SKOS.altLabel, name, language);
        }
    }

    private static void addIsPartOf(ArrayList<Resource> placeList) {
        ArrayList<Resource> childPlaces = new ArrayList<>();

        for (int i = placeList.size() - 1; i > 0; i--) {
            Resource crrResource = placeList.get(i);
            Resource parentPlace = placeList.get(i - 1);
            childPlaces.add(crrResource);

            for (Resource childPlace : childPlaces) {
                childPlace.addProperty(DCTerms.isPartOf, parentPlace);
            }
        }
    }

    private static void addHasPart(ArrayList<Resource> placeList) {
        ArrayList<Resource> parentPlaces = new ArrayList<>();

        for (int i = 0; i < placeList.size() - 1; i++) {
            Resource crrResource = placeList.get(i);
            Resource childPlace = placeList.get(i + 1);
            parentPlaces.add(crrResource);

            for (Resource parentPlace : parentPlaces) {
                parentPlace.addProperty(DCTerms.hasPart, childPlace);
            }
        }
    }
}
