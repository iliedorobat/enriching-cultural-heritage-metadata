package ro.webdata.lido.convert.edm.processing.leaf.eventComplexType.eventPlace;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.lido.convert.edm.common.constants.NSConstants;
import ro.webdata.lido.convert.edm.vocabulary.EDM;
import ro.webdata.lido.parser.core.leaf.appellationValue.AppellationValue;
import ro.webdata.lido.parser.core.leaf.eventPlace.EventPlace;
import ro.webdata.lido.parser.core.leaf.place.Place;
import ro.webdata.lido.parser.core.set.namePlaceSet.NamePlaceSet;

import java.util.ArrayList;

public class EventPlaceProcessing {
    /**
     * Generate the Event Place list
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param eventPlaceList The LIDO Event Place list
     * @return The RDF Event Place list
     */
    public ArrayList<Resource> generateEventPlaceList(Model model, Resource providedCHO, ArrayList<EventPlace> eventPlaceList) {
        ArrayList<Resource> placeList = new ArrayList<>();

        for (int i = 0; i < eventPlaceList.size(); i++) {
            Resource resource = generateEventPlace(model, providedCHO, eventPlaceList.get(i));
            placeList.add(resource);
        }

        return placeList;
    }

    /**
     * Generate an Event Place
     * @param model The RDF graph
     * @param providedCHO The RDF CHO
     * @param eventPlace The LIDO Event Place
     * @return The RDF Event Place
     */
    private Resource generateEventPlace(Model model, Resource providedCHO, EventPlace eventPlace) {
        Place place = eventPlace.getPlace();
        String placeType = place.getPoliticalEntity().getAttrValue();
        ArrayList<NamePlaceSet> NamePlaceSetList = place.getNamePlaceSet();

        Resource placeResource = model.createResource(
                providedCHO.getURI() + NSConstants.LINK_ID_EVENT + "/EventPlace"
        );
        placeResource.addProperty(RDF.type, EDM.Place);
        placeResource.addProperty(SKOS.note, placeType);

        for (int i = 0; i < NamePlaceSetList.size(); i++) {
            NamePlaceSet namePlaceSet = NamePlaceSetList.get(i);
            ArrayList<AppellationValue> appellationValueList = namePlaceSet.getAppellationValue();
            addEventPlaceName(model, placeResource, appellationValueList);
        }

        return placeResource;
    }

    /**
     * Add the <b>skos:prefLabel</b> property if not exists, or add the <b>skos:altLabel</b>
     * if the <b>skos:prefLabel</b> already exists in the Place Resource
     * @param model The RDF graph
     * @param placeResource The RDF Place Resource
     * @param appellationValueList The LIDO list with the place names
     */
    private void addEventPlaceName(
            Model model, Resource placeResource, ArrayList<AppellationValue> appellationValueList) {
        ArrayList<String> langList = new ArrayList<>();

        for (int j = 0; j < appellationValueList.size(); j++) {
            AppellationValue appellationValue = appellationValueList.get(j);
            String lang = appellationValue.getLang().getLang();
            String text = appellationValue.getText();
            Literal literal = model.createLiteral(text, lang);

            if (langList.indexOf(lang) == -1)
                placeResource.addProperty(SKOS.prefLabel, literal);
            else
                placeResource.addProperty(SKOS.altLabel, literal);
            langList.add(lang);
        }
    }
}
