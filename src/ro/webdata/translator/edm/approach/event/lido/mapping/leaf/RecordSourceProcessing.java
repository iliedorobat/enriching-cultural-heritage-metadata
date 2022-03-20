package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.parser.xml.lido.core.leaf.recordSource.RecordSource;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.LIDOType;

import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;
import static ro.webdata.translator.commons.Constants.ROMANIAN_COUNTRY_NAME;
import static ro.webdata.translator.edm.approach.event.lido.commons.ResourceUtils.addUriProperty;

public class RecordSourceProcessing {
    public static Resource generateDataProvider(Model model, ArrayList<RecordSource> recordSourceList) {
        int size = recordSourceList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(RecordSourceProcessing.class.getName() + ":" +
                        "\nThere has been received " + size + " \"lido:recordSource\" objects," +
                        " but EDM accepts only one agent object.");
            }

            RecordSource recordSource = recordSourceList.get(0);
            String recordSourceType = recordSource.getLidoType().getType();

            if (recordSourceType.equals(LIDOType.EUROPEANA_DATA_PROVIDER)) {
                String providerName = LegalBodyRefComplexTypeProcessing.getOrganizationName(recordSource.getLegalBodyName());

                if (providerName == null) {
                    System.err.println(RecordSourceProcessing.class.getName() + ":" +
                            "The valid \"lido:recordSource\" property value is missing.");
                    return null;
                }

                Resource dataProvider = model.createResource(
                        getNamespace(providerName) + Text.sanitizeString(providerName)
                );
                dataProvider.addProperty(RDF.type, FOAF.Organization);

                LegalBodyRefComplexTypeProcessing.addOrganizationName(model, dataProvider, recordSource.getLegalBodyName());
                LegalBodyRefComplexTypeProcessing.addOrganizationWeblink(model, dataProvider, recordSource.getLegalBodyWeblink());

                if (providerName.equals(Constants.INP_NAME)) {
                    addINPProperties(model, dataProvider);
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

    private static String getNamespace(String providerName) {
        String namespace = NS_REPO_RESOURCE_ORGANIZATION;

        if (providerName.equals(Constants.INP_NAME)) {
            namespace += ROMANIAN_COUNTRY_NAME + File.FILE_SEPARATOR;
        }

        return namespace;
    }

    private static void addINPProperties(Model model, Resource provider) {
        addUriProperty(model, provider, FOAF.homepage, "https://patrimoniu.ro/");
        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Const.LANG_EN);
        provider.addProperty(EDM.acronym, "INP", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "ensuring the legal framework and managing " +
                "the national cultural heritage", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "asigurarea cadrului legislativ si gestionarea " +
                "patrimoniului national cultural", Const.LANG_RO);

        addUriProperty(model, provider, EDM.organizationDomain, "culture");
        addUriProperty(model, provider, EDM.organizationSector, "cultural heritage");
        addUriProperty(model, provider, EDM.geographicLevel, "country");
        addUriProperty(model, provider, EDM.country, "Romania");

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);
    }
}
