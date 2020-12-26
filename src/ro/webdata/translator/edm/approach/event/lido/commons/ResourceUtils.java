package ro.webdata.translator.edm.approach.event.lido.commons;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Text;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMRoles;
import ro.webdata.translator.edm.approach.event.lido.commons.constants.Constants;

public final class ResourceUtils {
    /**
     * Generate the Europeana Provider (the institution which Europeana is harvesting for data)
     * @param model The RDF graph
     * @return The Europeana Provider
     */
    public static Resource generateProvider(Model model) {
        String providerLink = Namespace.NS_REPO_RESOURCE_ORGANIZATION
                + Text.sanitizeString("Politehnica University of Bucharest");
        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        provider.addProperty(DC_11.identifier, "https://upb.ro/en/", Const.LANG_EN);
        provider.addProperty(DC_11.identifier, "https://upb.ro/", Const.LANG_RO);

        provider.addProperty(FOAF.homepage, "https://upb.ro/en/", Const.LANG_EN);
        provider.addProperty(FOAF.homepage, "https://upb.ro/", Const.LANG_RO);

        provider.addProperty(SKOS.prefLabel, "Politehnica University of Bucharest", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "Universitatea Politehnica din București", Const.LANG_RO);

        provider.addProperty(EDM.acronym, "PUB", Const.LANG_EN);
        provider.addProperty(EDM.acronym, "UPB", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "academic research", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "cercetare academică", Const.LANG_RO);

        provider.addProperty(EDM.organizationDomain, "education", Const.LANG_EN);
        provider.addProperty(EDM.organizationDomain, "învățământ", Const.LANG_RO);

        provider.addProperty(EDM.organizationSector, "higher education", Const.LANG_EN);
        provider.addProperty(EDM.organizationSector, "învățământ superior", Const.LANG_RO);

        provider.addProperty(EDM.geographicLevel, "county", Const.LANG_EN);
        provider.addProperty(EDM.geographicLevel, "județ", Const.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);

        return provider;
    }

    /**
     * Generate the Europeana Intermediate Provider (the institution where are data stored)
     * @param model The RDF graph
     * @return The Europeana Intermediate Provider
     */
    public static Resource generateIntermediateProvider(Model model) {
        String providerLink = Namespace.NS_REPO_RESOURCE_ORGANIZATION
                + Text.sanitizeString("Romanian Open Data Portal");
        Resource provider = model.createResource(providerLink);
        provider.addProperty(RDF.type, FOAF.Organization);

        provider.addProperty(DC_11.identifier, Constants.DATA_GOV_LINK_EN, Const.LANG_EN);
        provider.addProperty(DC_11.identifier, Constants.DATA_GOV_LINK_RO, Const.LANG_RO);

        provider.addProperty(FOAF.homepage, Constants.DATA_GOV_LINK_EN, Const.LANG_EN);
        provider.addProperty(FOAF.homepage, Constants.DATA_GOV_LINK_RO, Const.LANG_RO);

        provider.addProperty(SKOS.prefLabel, "data.gov.ro", Const.LANG_EN);
        provider.addProperty(SKOS.prefLabel, "data.gov.ro", Const.LANG_RO);

        provider.addProperty(EDM.organizationScope, "centralization of open data published " +
                "by Romanian institutions according to the principles and standards in the field", Const.LANG_EN);
        provider.addProperty(EDM.organizationScope, "centralizarea datelor deschise publicate " +
                "de instituțiile din România conform principiilor și standardelor în domeniu", Const.LANG_RO);

        provider.addProperty(EDM.organizationDomain, "data publishing", Const.LANG_EN);
        provider.addProperty(EDM.organizationDomain, "publicarea datelor", Const.LANG_RO);

        provider.addProperty(EDM.organizationSector, "open data", Const.LANG_EN);
        provider.addProperty(EDM.organizationSector, "date deschise", Const.LANG_RO);

        provider.addProperty(EDM.geographicLevel, "country", Const.LANG_EN);
        provider.addProperty(EDM.geographicLevel, "țară", Const.LANG_RO);

        ResourceUtils.addProviderCountry(provider);

        provider.addProperty(EDM.europeanaRole, EDMRoles.ROLE_DATA_PROVIDER, Const.LANG_EN);

        return provider;
    }

    /**
     * Add Romania country for a provider from Romania
     * @param provider The provider, dataProvider or intermediateProvider
     */
    //TODO: automatize
    public static void addProviderCountry(Resource provider) {
        provider.addProperty(EDM.country, "Romania", Const.LANG_EN);
        provider.addProperty(EDM.country, "România", Const.LANG_RO);
    }
}
