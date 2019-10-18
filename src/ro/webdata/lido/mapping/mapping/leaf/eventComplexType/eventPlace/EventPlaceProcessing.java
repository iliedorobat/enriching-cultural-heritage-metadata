package ro.webdata.lido.mapping.mapping.leaf.eventComplexType.eventPlace;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.mapping.common.ResourceUtils;
import ro.webdata.lido.mapping.common.constants.NSConstants;
import ro.webdata.lido.mapping.common.http.RequestUtils;
import ro.webdata.lido.mapping.vocabulary.EDM;
import ro.webdata.lido.parser.core.complex.placeComplexType.PlaceComplexType;
import ro.webdata.lido.parser.core.leaf.appellationValue.AppellationValue;
import ro.webdata.lido.parser.core.leaf.eventPlace.EventPlace;
import ro.webdata.lido.parser.core.leaf.partOfPlace.PartOfPlace;
import ro.webdata.lido.parser.core.leaf.place.Place;
import ro.webdata.lido.parser.core.set.namePlaceSet.NamePlaceSet;

import java.util.ArrayList;

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
    //TODO: disambiguating (we can find the same locality name in more than one county)
    // use the http://www.geonames.org/ API
    private ArrayList<Resource> generateEventPlace(Model model, Place place) {
        ArrayList<Resource> resourceList = addEventPlaceNames(model, place);
        addPartOfPlace(model, place, resourceList);

        return resourceList;
    }

    /**
     * Add all places that contain the initial place<br/>
     * E.g.: initial place: Moinesti city:<br/>
     *      * Moinesti is in Bacau county<br/>
     *      * Bacau county is in Moldova region<br/>
     *      * Moldova region is in Romania
     * @param model The RDF graph
     * @param place The place
     * @param resourceList The list with Event Places
     */
    private void addPartOfPlace(Model model, PlaceComplexType place, ArrayList<Resource> resourceList) {
        ArrayList<PartOfPlace> partOfPlaceList = place.getPartOfPlace();

        if (partOfPlaceList != null) {
            for (int i = 0; i < partOfPlaceList.size(); i++) {
                PartOfPlace partOfPlace = partOfPlaceList.get(i);
                resourceList.addAll(
                        addEventPlaceNames(model, partOfPlace)
                );
                addPartOfPlace(model, partOfPlace, resourceList);
            }
        }
    }

    /**
     * Generate the Event Place list for every name of a specified place
     * @param model The RDF graph
     * @param place The place
     * @return The list of event places
     */
    private ArrayList<Resource> addEventPlaceNames(Model model, PlaceComplexType place) {
        ArrayList<Resource> resourceList = new ArrayList<>();
        ArrayList<NamePlaceSet> NamePlaceSetList = place.getNamePlaceSet();
        String placeType = place.getPoliticalEntity().getAttrValue();

        for (int i = 0; i < NamePlaceSetList.size(); i++) {
            NamePlaceSet namePlaceSet = NamePlaceSetList.get(i);
            ArrayList<AppellationValue> appellationValueList = namePlaceSet.getAppellationValue();
            resourceList.add(
                    consolidateEventPlace(model, appellationValueList, placeType)
            );
        }

        return resourceList;
    }

    /**
     * Add the <b>skos:prefLabel</b> property if not exists, or add the <b>skos:altLabel</b>
     * if the <b>skos:prefLabel</b> already exists in the Place Resource
     * @param model The RDF graph
     * @param appellationValueList The LIDO list with the place names
     * @param placeType The political entity ("point", "locality" etc.)
     * @return The consolidated place resource
     */
    private Resource consolidateEventPlace(
            Model model, ArrayList<AppellationValue> appellationValueList, String placeType) {
        ArrayList<String> langList = new ArrayList<>();
        Resource placeResource = null;

        for (int i = 0; i < appellationValueList.size(); i++) {
            AppellationValue appellationValue = appellationValueList.get(i);
            String lang = appellationValue.getLang().getLang();
            String placeName = appellationValue.getText();
            Literal literal = model.createLiteral(placeName, lang);

            if (placeResource == null)
                placeResource = createEventPlace(model, placeName, placeType);
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
     * @param placeName The place name (e.g. "Romania", "Beroe Piatra Frecăței" etc.)
     * @param placeType The political entity ("point", "locality" etc.)
     * @return The place resource
     */
    private Resource createEventPlace(Model model, String placeName, String placeType) {
        String localLink = ResourceUtils.generateURI(NSConstants.NS_REPO_RESOURCE_PLACE, placeName);
        String dbpediaLink = ResourceUtils.generateDBPediaURI(placeName);
        Resource placeResource = model.createResource(localLink);

        if (RequestUtils.isValidDBPedia(placeName)) {
            placeResource.addProperty(OWL2.sameAs, model.createResource(dbpediaLink));
        }

        placeResource.addProperty(RDF.type, EDM.Place);
        placeResource.addProperty(SKOS.note, placeType);

        return placeResource;
    }
}
