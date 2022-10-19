package ro.webdata.echo.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.RecordWrapProcessing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.PlaceType;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.echo.translator.edm.approach.event.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.RecordRightsProcessing;
import ro.webdata.echo.translator.edm.approach.event.lido.mapping.leaf.RecordSourceProcessing;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_ORGANIZATION;
import static ro.webdata.echo.translator.edm.approach.event.lido.commons.ResourceUtils.addUriProperty;

public class RecordWrapProcessing {
    private static final String CIMEC_LINK = "http://www.cimec.ro";

    public static void mapEntries(
            Model model,
            Resource aggregation,
            Resource providedCHO,
            RecordWrap recordWrap
    ) {
        Resource dataProvider = RecordSourceProcessing.generateDataProvider(model, recordWrap.getRecordSource());
        Resource provider = generateProvider(model);
        Resource intermediateProvider = generateIntermediateProvider(model);
        Resource license = RecordRightsProcessing.getLicense(model, recordWrap.getRecordRights());

        aggregation.addProperty(EDM.aggregatedCHO, providedCHO);
        aggregation.addProperty(EDM.provider, provider);
        aggregation.addProperty(EDM.intermediateProvider, intermediateProvider);
        aggregation.addProperty(EDM.dataProvider, dataProvider);
        aggregation.addProperty(EDM.rights, license);
    }

    /**
     * Generate the Europeana Provider (the aggregator which Europeana harvests for data)
     * One of the following: https://pro.europeana.eu/page/aggregators
     * @param model The RDF graph
     * @return The Europeana Provider
     */
    private static Resource generateProvider(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + File.FILE_SEPARATOR
                + Text.sanitizeString("Institutul Național al Patrimoniului");
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        addUriProperty(model, provider, DC_11.identifier, CIMEC_LINK);
        addUriProperty(model, provider, FOAF.homepage, CIMEC_LINK);
        addUriProperty(model, provider, FOAF.logo, "https://cimec.ro/wp-content/uploads/2021/08/logo-650-2.jpg");

        provider.addProperty(SKOS.prefLabel, "National Heritage Institute", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Institutul Național al Patrimoniului", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "CIMEC", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "cercetarea, protejarea și restaurarea patirmoniului cultural român", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "research, protect and restore the cultural heritage of Romania", Const.LANG_RO);

        addUriProperty(model, provider, EDM.organizationDomain, "public institution");
        addUriProperty(model, provider, EDM.organizationSector, "cultural heritage");
        addUriProperty(model, provider, EDM.geographicLevel, "country");
        addUriProperty(model, provider, EDM.country, "Romania");

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_PROVIDER, Const.LANG_EN);

        return provider;
    }

    /**
     * Generate the Europeana Intermediate Provider (the institution which curates the data)
     * @param model The RDF graph
     * @return The Europeana Intermediate Provider
     */
    private static Resource generateIntermediateProvider(Model model) {
        String relativeUri = PlaceType.COUNTRY + ":Romania"
                + File.FILE_SEPARATOR
                + Text.sanitizeString("Politehnica University of Bucharest");
        String providerLink = URIUtils.prepareUri(NS_REPO_RESOURCE_ORGANIZATION, relativeUri);

        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        addUriProperty(model, provider, DC_11.identifier, "https://upb.ro/en/");
        addUriProperty(model, provider, FOAF.homepage, "https://upb.ro/en/");
        addUriProperty(model, provider, FOAF.logo, "https://upb.ro/upb-identitate-vizuala-logo/");

        provider.addProperty(SKOS.prefLabel, "Politehnica University of Bucharest", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Universitatea Politehnica din București", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "PUB", Const.LANG_EN);
        provider.addProperty(EDM.acronym, "UPB", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "academic research", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "cercetare academică", Const.LANG_RO);

        addUriProperty(model, provider, EDM.organizationDomain, "education");
        addUriProperty(model, provider, EDM.organizationSector, "higher education");
        addUriProperty(model, provider, EDM.geographicLevel, "country");
        addUriProperty(model, provider, EDM.country, "Romania");

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);

        return provider;
    }
}
