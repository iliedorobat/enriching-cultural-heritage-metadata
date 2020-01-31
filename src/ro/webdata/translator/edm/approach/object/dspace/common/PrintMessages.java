package ro.webdata.translator.edm.approach.object.dspace.common;

import org.apache.jena.rdf.model.Resource;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;

public class PrintMessages {
    public static void elementWarning(String operation, Resource providedCHO, DcValue dcValue) {
        String element = dcValue.getElement().getValue();
        String qualifier = dcValue.getQualifier().getValue();

        System.err.println("WARNING: " + operation + ": RECORD IDENTIFIER: " + providedCHO.getURI() + "\n"
                + "\tThe qualifier \"" + qualifier + "\" is not one of the \"" + element + "\" accepted qualifiers!\n"
                + "\tPlease consider to use a proper qualifier for the \"" + element + "\" element!");
    }
}
