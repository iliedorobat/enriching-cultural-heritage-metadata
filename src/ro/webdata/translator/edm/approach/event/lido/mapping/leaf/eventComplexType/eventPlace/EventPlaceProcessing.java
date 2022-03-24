package ro.webdata.translator.edm.approach.event.lido.mapping.leaf.eventComplexType.eventPlace;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.parser.xml.lido.core.leaf.eventPlace.EventPlace;
import ro.webdata.parser.xml.lido.core.leaf.place.Place;
import ro.webdata.translator.edm.approach.event.lido.commons.PlaceMapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventPlaceProcessing {
    /**
     * Generate the list of Event Place for all places
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param eventPlaceList The LIDO Event Place list
     * @return The RDF Event Place list
     */
    public static ArrayList<Resource> generateEventPlaceList(
            Model model, Resource providedCHO, ArrayList<EventPlace> eventPlaceList
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

        addIsPartOf(allPlaceList);
        // TODO: "dcterms:hasPart"
//        addHasPart(allPlaceList);

        return placeList;
    }

    // TODO: owl:sameAs => use geo names (https://www.geonames.org/search.html?q=&country=RO)
    private static Resource generateEventPlace(Model model, Map.Entry<String, HashMap<String, Object>> placeMap) {
        HashMap<String, String> nameMap = (HashMap<String, String>) placeMap.getValue().get(PlaceMapUtils.NAME_PROP);
        String uri = placeMap.getValue().get(PlaceMapUtils.URI_PROP).toString();
        String placeType = placeMap.getKey();

        Resource placeResource = model.createResource(uri);

        for (Map.Entry<String, String> entry : nameMap.entrySet()) {
            String lang = entry.getKey();
            String name = entry.getValue();
            addLabelProperty(placeResource, name, lang);
        }

        placeResource.addProperty(SKOS.note, placeType, Const.LANG_EN);

//        // TODO: link the place to DBpedia
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
        for (int i = placeList.size() - 1; i > 0; i--) {
            Resource crrResource = placeList.get(i);
            Resource parentPlace = placeList.get(i - 1);
            crrResource.addProperty(DCTerms.isPartOf, parentPlace);
        }
    }

    private static void addHasPart(ArrayList<Resource> placeList) {
        for (int i = 0; i < placeList.size() - 1; i++) {
            Resource crrResource = placeList.get(i);
            Resource childPlace = placeList.get(i + 1);
            crrResource.addProperty(DCTerms.hasPart, childPlace);
        }
    }
}
