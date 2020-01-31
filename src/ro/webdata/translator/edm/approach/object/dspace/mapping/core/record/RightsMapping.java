package ro.webdata.translator.edm.approach.object.dspace.mapping.core.record;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.parser.xml.dspace.core.attribute.record.BasicRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.translator.edm.approach.event.lido.common.ResourceUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.object.dspace.common.PrintMessages;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.EnvConstants;

public class RightsMapping {
    private static final String REFINEMENT_HOLDER = "holder";

    public static void processing(Model model, Resource providedCHO, DcValue dcValue) {
        String language = dcValue.getLanguage().getValue();
        String qualifier = dcValue.getQualifier().getValue();
        String value = dcValue.getText();

        switch (qualifier) {
            case BasicRecord.EMPTY:
            case BasicRecord.NONE:
                providedCHO.addProperty(DC_11.rights, value, language);
                break;
            case REFINEMENT_HOLDER:
                String holderUri = ResourceUtils.generateURI(
                        NSConstants.SIMPLE_NS_REPO_RESOURCE, EDM.Agent, value
                );
                Resource holder = model
                        .createResource(holderUri)
                        .addProperty(RDF.type, EDM.Agent)
                        .addProperty(SKOS.prefLabel, value, language);
                //TODO: should the edm:currentLocation get a reference to a Place entity?
                providedCHO.addProperty(EDM.currentLocation, holder);
                break;
            default:
                PrintMessages.elementWarning(EnvConstants.OPERATION_MAPPING, providedCHO, dcValue);
                break;
        }
    }
}
