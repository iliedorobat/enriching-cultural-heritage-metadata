package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.parser.xml.lido.core.leaf.recordSource.RecordSource;
import ro.webdata.translator.edm.approach.event.lido.commons.ResourceUtils;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.LIDOType;

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

            if (recordSourceType.equals(LIDOType.EUROPEANA_DATA_PROVIDER)) {
                String providerName = legalBodyRefComplexTypeProcessing.getOrganizationName(recordSource.getLegalBodyName());
                Resource dataProvider = model.createResource(
                        Namespace.NS_REPO_RESOURCE_ORGANIZATION + Text.sanitizeString(providerName)
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
                        LIDOType.EUROPEANA_DATA_PROVIDER + ", but have been received " +
                        recordSourceType + ".");
            }
        }

        return null;
    }

    private void addINPProperties(Resource provider) {
        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Const.LANG_EN);

        provider.addProperty(FOAF.homepage, "https://patrimoniu.ro/", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "INP", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "ensuring the legal framework and managing " +
                "the national cultural heritage", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "asigurarea cadrului legislativ si gestionarea " +
                "patrimoniului national cultural", Const.LANG_RO);

        provider.addProperty(EDM.organizationDomain, "culture", Const.LANG_EN);
        provider.addProperty(EDM.organizationDomain, "cultură", Const.LANG_RO);

        provider.addProperty(EDM.organizationSector, "cultural heritage", Const.LANG_EN);
        provider.addProperty(EDM.organizationSector, "patrimoniul cultural", Const.LANG_RO);

        provider.addProperty(EDM.geographicLevel, "country", Const.LANG_EN);
        provider.addProperty(EDM.geographicLevel, "țară", Const.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);
    }
}
