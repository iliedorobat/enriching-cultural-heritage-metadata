package ro.webdata.lido.mapping.mapping.leaf.eventComplexType.eventPlace;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.mapping.common.ResourceUtils;
import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.common.constants.NSConstants;
import ro.webdata.lido.mapping.common.constants.PlaceConstants;
import ro.webdata.lido.mapping.vocabulary.EDM;
import ro.webdata.lido.parser.core.complex.placeComplexType.PlaceComplexType;
import ro.webdata.lido.parser.core.leaf.appellationValue.AppellationValue;
import ro.webdata.lido.parser.core.leaf.eventPlace.EventPlace;
import ro.webdata.lido.parser.core.leaf.partOfPlace.PartOfPlace;
import ro.webdata.lido.parser.core.leaf.place.Place;
import ro.webdata.lido.parser.core.set.namePlaceSet.NamePlaceSet;

import java.util.*;

public class EventPlaceProcessing {
    /**
     * Generate the list of Event Place for all places
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param eventPlaceList The LIDO Event Place list
     * @return The RDF Event Place list
     */
    public ArrayList<Resource> generateEventPlaceList(
            Model model, Resource providedCHO, ArrayList<EventPlace> eventPlaceList) {
        ArrayList<Resource> placeList = new ArrayList<>();

        for (int i = 0; i < eventPlaceList.size(); i++) {
            EventPlace eventPlace = eventPlaceList.get(i);
            ArrayList<Resource> resourceList = generateEventPlace(model, eventPlace.getPlace());
            placeList.addAll(resourceList);
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
    private ArrayList<Resource> generateEventPlace(Model model, Place place) {
        LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap =  preparePlaceMap(place);
        ArrayList<Resource> resourceList = generatePlaceList(model, placeMap);
        return resourceList;
    }

    /**
     * Prepare a sorted key-value list which contains all places.<br/>
     * The sorting is based on the principle of "top level" political entities
     * (E.g.: country, region, county, commune, locality, point)
     * @param place The related place
     */
    private LinkedHashMap<String, ArrayList<PlaceComplexType>> preparePlaceMap(PlaceComplexType place) {
        LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap = new LinkedHashMap<>();
        ArrayList<PartOfPlace> partOfPlaceList = place.getPartOfPlace();

        if (partOfPlaceList != null) {
            for (PartOfPlace partOfPlace : partOfPlaceList) {
                placeMap.putAll(
                        preparePlaceMap(partOfPlace)
                );
            }
        }

        pushPlaceByType(place, placeMap);

        return placeMap;
    }

    /**
     * Add a place into the sorted key-value list of places
     * @param place The related place
     * @param placeMap A sorted key-value list of places
     */
    private void pushPlaceByType(PlaceComplexType place, HashMap<String, ArrayList<PlaceComplexType>> placeMap) {
        String placeType = place.getPoliticalEntity().getAttrValue();
        ArrayList<PlaceComplexType> placeValueList = placeMap.containsKey(placeType)
                ? placeMap.get(placeType)
                : new ArrayList<>();
        placeValueList.add(place);

        if (!placeMap.containsKey(placeType))
            placeMap.put(placeType, placeValueList);
    }

    /**
     * Generate a list with all political places related to an event<br/>
     * E.g.: initial place: point "Leisure Island" from locality Bacau:<br/>
     *      * locality Bacau is part of Bacau county<br/>
     *      * Bacau county is part of Moldova region<br/>
     *      * Moldova region is part of Romania
     * @param model The RDF graph
     * @param placeMap A sorted key-value list of places
     */
    private ArrayList<Resource> generatePlaceList(
            Model model, LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap) {
        ArrayList<Resource> resourceList = new ArrayList<>();

        for (Map.Entry<String, ArrayList<PlaceComplexType>> entry : placeMap.entrySet()) {
            resourceList.addAll(
                    generateEventPlacesNames(model, placeMap, entry.getValue())
            );
        }

        return resourceList;
    }

    /**
     * Generate a list that contains every name of a all places
     * @param model The RDF graph
     * @param placeMap A sorted key-value list of places
     * @param placeList The list of places
     * @return The list of event places
     */
    private ArrayList<Resource> generateEventPlacesNames(
            Model model,
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap,
            ArrayList<PlaceComplexType> placeList
    ) {
        ArrayList<Resource> resourceList = new ArrayList<>();

        for (PlaceComplexType place : placeList) {
            resourceList.addAll(
                    generateEventPlaceNames(model, placeMap, place)
            );
        }

        return resourceList;
    }

    /**
     * Generate a list that contains every name of a specified place
     * @param model The RDF graph
     * @param placeMap A sorted key-value list of places
     * @param place The specified place
     * @return The list of RDF Resources for a specified place
     */
    private ArrayList<Resource> generateEventPlaceNames(
            Model model,
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap,
            PlaceComplexType place
    ) {
        ArrayList<Resource> resourceList = new ArrayList<>();
        ArrayList<NamePlaceSet> NamePlaceSetList = place.getNamePlaceSet();
        String placeType = place.getPoliticalEntity().getAttrValue();

        for (int i = 0; i < NamePlaceSetList.size(); i++) {
            NamePlaceSet namePlaceSet = NamePlaceSetList.get(i);
            ArrayList<AppellationValue> appellationValueList = namePlaceSet.getAppellationValue();
            resourceList.add(
                    consolidateEventPlace(model, placeMap, appellationValueList, placeType)
            );
        }

        return resourceList;
    }

    /**
     * Add the <b>skos:prefLabel</b> property if not exists, or add the <b>skos:altLabel</b>
     * if the <b>skos:prefLabel</b> already exists in the Place Resource
     * @param model The RDF graph
     * @param placeMap A sorted key-value list of places
     * @param appellationValueList The LIDO list with the place names
     * @param placeType The political entity ("point", "locality" etc.)
     * @return The consolidated place resource
     */
    //TODO: "dcterms:hasPart" & "dcterms:isPartOf"
    private Resource consolidateEventPlace(
            Model model,
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap,
            ArrayList<AppellationValue> appellationValueList,
            String placeType
    ) {
        ArrayList<String> langList = new ArrayList<>();
        Resource placeResource = null;

        for (int i = 0; i < appellationValueList.size(); i++) {
            AppellationValue appellationValue = appellationValueList.get(i);
            String lang = appellationValue.getLang().getLang();
            String placeName = appellationValue.getText();
            Literal literal = model.createLiteral(placeName, lang);

            if (placeResource == null)
                placeResource = createEventPlace(model, placeMap, placeName, placeType);
            Property label = langList.indexOf(lang) == -1
                    ? SKOS.prefLabel
                    : SKOS.altLabel;
            placeResource.addProperty(label, literal);

            langList.add(lang);
        }

        return placeResource;
    }

    /**
     * Create the basic <b>edm:Place</b> resource
     * @param model The RDF graph
     * @param placeMap A sorted key-value list of places
     * @param placeName The place name (e.g. "Romania", "Beroe Piatra Frecăței" etc.)
     * @param placeType The political entity ("point", "locality" etc.)
     * @return The place resource
     */
    //TODO: use geo names (https://www.geonames.org/search.html?q=&country=RO) for places
    private Resource createEventPlace(
            Model model,
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap,
            String placeName,
            String placeType
    ) {
        String localLink = ResourceUtils.generateURI(
                NSConstants.NS_REPO_RESOURCE_PLACE,
                getPlaceName(placeMap, placeName, placeType)
        );
        Resource placeResource = model.createResource(localLink);

        //TODO: add it back
//        String dbpediaLink = ResourceUtils.generateDBPediaURI(placeName);
//        if (RequestUtils.isValidDBPedia(placeName)) {
//            placeResource.addProperty(OWL2.sameAs, model.createResource(dbpediaLink));
//        }

        placeResource.addProperty(RDF.type, EDM.Place);
        placeResource.addProperty(SKOS.note, placeType);

        return placeResource;
    }

    //TODO: find a better way to build the name of points, localities and communes
    private String getPlaceName(
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap,
            String placeName,
            String placeType
    ) {
        String parentName;

        switch (placeType) {
            case PlaceConstants.PLACE_TYPE_COMMUNE:
                try {
                    parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_COUNTY);
                } catch (Exception e) {
                    try { parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_REGION); }
                    catch (Exception e2) { parentName = placeName; }
                }
                return placeName + Constants.UNDERSCORE_PLACEHOLDER + getPlaceName(placeMap, parentName, PlaceConstants.PLACE_TYPE_COUNTY);
            case PlaceConstants.PLACE_TYPE_LOCALITY:
                try {
                    parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_COMMUNE);
                } catch (Exception e) {
                    try {
                        parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_COUNTY);
                    } catch (Exception e2) {
                        try { parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_REGION); }
                        catch (Exception e3) { parentName = placeName; }
                    }
                }
                return placeName + Constants.UNDERSCORE_PLACEHOLDER + getPlaceName(placeMap, parentName, PlaceConstants.PLACE_TYPE_COMMUNE);
            case PlaceConstants.PLACE_TYPE_POINT:
                try {
                    parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_LOCALITY);
                } catch (Exception e2) {
                    try {
                        parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_COMMUNE);
                    } catch (Exception e3) {
                        try {
                            parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_COUNTY);
                        } catch (Exception e4) {
                            try { parentName = getFirstParentName(placeMap, PlaceConstants.PLACE_TYPE_REGION); }
                            catch (Exception e5) { parentName = placeName; }
                        }
                    }
                }
                return placeName + Constants.UNDERSCORE_PLACEHOLDER + getPlaceName(placeMap, parentName, PlaceConstants.PLACE_TYPE_LOCALITY);
            default:
                return placeName;
        }
    }

    private String getFirstParentName(
            LinkedHashMap<String, ArrayList<PlaceComplexType>> placeMap, String parentPlaceType) {
        return placeMap.get(parentPlaceType).get(0)
                .getNamePlaceSet().get(0)
                .getAppellationValue().get(0)
                .getText();
    }

    private String getParentType(String placeType) {
        List<String> placeTypeList = Arrays.asList(PlaceConstants.PLACE_TYPE_LIST);
        int index = placeTypeList.indexOf(placeType);

        if (index == placeTypeList.size() - 1) {
            return null;
        }

        return placeTypeList.get(index + 1);
    }

    private String getChildType(String placeType) {
        List<String> placeTypeList = Arrays.asList(PlaceConstants.PLACE_TYPE_LIST);
        int index = placeTypeList.indexOf(placeType);

        if (index == 0) {
            return null;
        }

        return placeTypeList.get(index - 1);
    }
}
