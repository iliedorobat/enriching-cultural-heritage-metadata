package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.parser.xml.lido.core.leaf.recordSource.RecordSource;
import ro.webdata.translator.edm.approach.event.lido.common.ResourceUtils;
import ro.webdata.translator.edm.approach.event.lido.common.TextUtils;
import ro.webdata.translator.edm.approach.event.lido.common.constants.*;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM2;

import java.util.ArrayList;

public class RecordSourceProcessing {
    private static LegalBodyRefComplexTypeProcessing legalBodyRefComplexTypeProcessing = new LegalBodyRefComplexTypeProcessing();

    public Resource generateDataProvider(Model model, ArrayList<RecordSource> recordSourceList) {
        int size = recordSourceList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(this.getClass().getName() + ":" +
                        "\nThere has been received " + size + " \"lido:recordSource\" objects," +
                        " but EDM accepts only one agent object.");
            }

            RecordSource recordSource = recordSourceList.get(0);
            String recordSourceType = recordSource.getType().getType();

            if (recordSourceType.equals(LIDOConstants.LIDO_TYPE_EUROPEANA_DATA_PROVIDER)) {
                String providerName = legalBodyRefComplexTypeProcessing.getOrganizationName(recordSource.getLegalBodyName());
                Resource dataProvider = model.createResource(
                        NSConstants.NS_REPO_RESOURCE_ORGANIZATION
                        + FileConstants.FILE_SEPARATOR + TextUtils.sanitizeString(providerName)
                );
                dataProvider.addProperty(RDF.type, FOAF.Organization);

                legalBodyRefComplexTypeProcessing.addOrganizationName(model, dataProvider, recordSource.getLegalBodyName());
                legalBodyRefComplexTypeProcessing.addOrganizationWeblink(model, dataProvider, recordSource.getLegalBodyWeblink());

                if (providerName.equals(Constants.INP_NAME))
                    addINPProperties(dataProvider);

                return dataProvider;
            } else {
                System.err.println(this.getClass().getName() + ":" +
                        "The valid \"lido:type\" property value is " +
                        LIDOConstants.LIDO_TYPE_EUROPEANA_DATA_PROVIDER + ", but have been received " +
                        recordSourceType + ".");
            }
        }

        return null;
    }

    private void addINPProperties(Resource provider) {
        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Constants.LANG_EN);

        provider.addProperty(FOAF.homepage, "https://patrimoniu.ro/", Constants.LANG_RO);

        provider.addProperty(EDM2.acronym, "INP", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationScope, "ensuring the legal framework and managing " +
                "the national cultural heritage", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationScope, "asigurarea cadrului legislativ si gestionarea " +
                "patrimoniului national cultural", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationDomain, "culture", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationDomain, "cultură", Constants.LANG_RO);

        provider.addProperty(EDM2.organizationSector, "cultural heritage", Constants.LANG_EN);
        provider.addProperty(EDM2.organizationSector, "patrimoniul cultural", Constants.LANG_RO);

        provider.addProperty(EDM2.geographicLevel, "country", Constants.LANG_EN);
        provider.addProperty(EDM2.geographicLevel, "țară", Constants.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM2.europeanaRole, EDMConstants.ROLE_DATA_PROVIDER, Constants.LANG_EN);
    }
}
