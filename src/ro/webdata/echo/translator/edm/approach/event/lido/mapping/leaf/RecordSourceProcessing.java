package ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.constants.LIDOType;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.RecordWrapProcessing.CimecProvider;
import ro.webdata.parser.xml.lido.core.leaf.recordSource.RecordSource;

import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;

public class RecordSourceProcessing {
    public static final String INP_NAME_RO = "Institutul Național al Patrimoniului";

    public static Resource generateDataProvider(Model model, ArrayList<RecordSource> recordSourceList) {
        int size = recordSourceList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(RecordSourceProcessing.class.getName() + ":" +
                        "\nThere has been received " + size + " \"lido:recordSource\" objects," +
                        " but EDM accepts only one agent object.");
            }

            RecordSource recordSource = recordSourceList.get(0);
            String recordSourceType = recordSource.getType().getType();

            if (recordSourceType.equals(LIDOType.EUROPEANA_DATA_PROVIDER)) {
                Resource dataProvider = null;
                String providerName = LegalBodyRefComplexTypeProcessing.getOrganizationName(recordSource.getLegalBodyName());

                if (providerName == null) {
                    System.err.println(RecordSourceProcessing.class.getName() + ":" +
                            "The valid \"lido:recordSource\" property value is missing.");
                    return null;
                } else if (providerName.equals(INP_NAME_RO)) {
                    dataProvider = CimecProvider.generateAgent(model);
                } else {
                    String uri = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, Text.sanitizeString(providerName));
                    dataProvider = model
                            .createResource(uri)
                            .addProperty(RDF.type, EDM.Agent);
                    LegalBodyRefComplexTypeProcessing.addOrganizationName(model, dataProvider, recordSource.getLegalBodyName());
                    LegalBodyRefComplexTypeProcessing.addOrganizationWeblink(model, dataProvider, recordSource.getLegalBodyWeblink());
                }

                return dataProvider;
            } else {
                System.err.println(RecordSourceProcessing.class.getName() + ":" +
                        "The valid \"lido:type\" property value is " +
                        LIDOType.EUROPEANA_DATA_PROVIDER + ", but have been received " +
                        recordSourceType + ".");
            }
        }

        return null;
    }
}
