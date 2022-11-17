package ro.webdata.echo.translator.edm.approach.object.dspace.commons;

import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.graph.vocab.constraints.EDMType;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public final class PrintMessages {
    public static void missingEdmType(String operation, Resource providedCHO) {
        System.err.println("ERROR: " + operation + ": RECORD IDENTIFIER: " + providedCHO.getURI() + "\n"
                + "\tedm:type property is missing!\n"
                + "\tPlease consider to use a proper value for the edm:type property "
                + "(" + EDMType.VALUES.toString() + ")!");
    }

    public static void invalidEdmType(String operation, Resource providedCHO, String value) {
        System.err.println("WARNING: " + operation + ": RECORD IDENTIFIER: " + providedCHO.getURI() + "\n"
                + "\tThe value \"" + value + "\" is not one of the edm:type accepted values!\n"
                + "\tPlease consider to use a proper value for the edm:type property "
                + "(" + EDMType.VALUES.toString() + ")!");
    }

    public static void invalidElement(String operation, Resource providedCHO, DcValue dcValue) {
        String element = dcValue.getElement().getValue();
        String qualifier = dcValue.getQualifier().getValue();

        System.err.println("WARNING: " + operation + ": RECORD IDENTIFIER: " + providedCHO.getURI() + "\n"
                + "\tThe qualifier \"" + qualifier + "\" is not one of the \"" + element + "\" accepted qualifiers!\n"
                + "\tPlease consider to use a proper qualifier for the \"" + element + "\" element!");
    }
}
