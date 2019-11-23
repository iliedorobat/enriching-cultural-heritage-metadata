package ro.webdata.lido.translator.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class CC {
    private static final Model m = ModelFactory.createDefaultModel();
    public static final String NS = "http://creativecommons.org/ns#";

    //Classes
    public static final Resource Work;
    public static final Resource License;
    public static final Resource Jurisdiction;
    public static final Resource Permission;
    public static final Resource Requirement;
    public static final Resource Prohibition;

    //Permissions
    public static final Resource Reproduction;
    public static final Resource Distribution;
    public static final Resource DerivativeWorks;
    public static final Resource Sharing;

    //Requirements
    public static final Resource Notice;
    public static final Resource Attribution;
    public static final Resource ShareAlike;
    public static final Resource SourceCode;
    public static final Resource Copyleft;
    public static final Resource LesserCopyleft;

    //Prohibitions
    public static final Resource CommercialUse;
    public static final Resource HighIncomeNationUse;

    //License Properties
    public static final Property permits;
    public static final Property requires;
    public static final Property prohibits;
    public static final Property jurisdiction;
    public static final Property legalcode;
    public static final Property deprecatedOn;

    //Work Properties
    public static final Property license;
    public static final Property morePermissions;
    public static final Property attributionName;
    public static final Property attributionURL;
    public static final Property useGuidelines;

    public static String getURI() {
        return "http://creativecommons.org/ns#";
    }

    static {
        Work = m.createResource(NS + "Work");
        License = m.createResource(NS + "License");
        Jurisdiction = m.createResource(NS + "Jurisdiction");
        Permission = m.createResource(NS + "Permission");
        Requirement = m.createResource(NS + "Requirement");
        Prohibition = m.createResource(NS + "Prohibition");
        Reproduction = m.createResource(NS + "Reproduction");
        Distribution = m.createResource(NS + "Distribution");
        DerivativeWorks = m.createResource(NS + "DerivativeWorks");
        Sharing = m.createResource(NS + "Sharing");
        Notice = m.createResource(NS + "Notice");
        Attribution = m.createResource(NS + "Attribution");
        ShareAlike = m.createResource(NS + "ShareAlike");
        SourceCode = m.createResource(NS + "SourceCode");
        Copyleft = m.createResource(NS + "Copyleft");
        LesserCopyleft = m.createResource(NS + "LesserCopyleft");
        CommercialUse = m.createResource(NS + "CommercialUse");
        HighIncomeNationUse = m.createResource(NS + "HighIncomeNationUse");

        permits = m.createProperty(NS + "permits");
        requires = m.createProperty(NS + "requires");
        prohibits = m.createProperty(NS + "prohibits");
        jurisdiction = m.createProperty(NS + "jurisdiction");
        legalcode = m.createProperty(NS + "legalcode");
        deprecatedOn = m.createProperty(NS + "deprecatedOn");
        license = m.createProperty(NS + "license");
        morePermissions = m.createProperty(NS + "morePermissions");
        attributionName = m.createProperty(NS + "attributionName");
        attributionURL = m.createProperty(NS + "attributionURL");
        useGuidelines = m.createProperty(NS + "useGuidelines");
    }
}
