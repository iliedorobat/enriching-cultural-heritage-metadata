package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.parser.xml.lido.core.leaf.recordRights.RecordRights;
import ro.webdata.parser.xml.lido.core.leaf.rightsType.RightsType;
import ro.webdata.parser.xml.lido.core.leaf.term.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordRightsProcessing {
    public Resource getLicense(Model model, ArrayList<RecordRights> recordRightsList) {
        int size = recordRightsList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(this.getClass().getName() + ":" +
                        "\nThere has been received " + size + " \"lido:recordRights\" objects," +
                        " but EDM accepts only one license object.");
            }

            return getLicense(model, recordRightsList.get(0));
        }

        return null;
    }

    private Resource getLicense(Model model, RecordRights recordRights) {
        ArrayList<RightsType> rightsTypeList = recordRights.getRightsType();
        int size = rightsTypeList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(this.getClass().getName() + ":" +
                        "\nThere has been received " + size + " \"lido:rightsType\" objects," +
                        " but EDM accepts only one license object.");
            }

            return getLicense(model, rightsTypeList.get(0));
        }

        return null;
    }

    private Resource getLicense(Model model, RightsType rightsType) {
        ArrayList<Term> termList = rightsType.getTerm();
        int size = termList.size();

        if (size > 0) {
            if (size > 1) {
                System.err.println(this.getClass().getName() + ":" +
                        "\nThere has been received " + size + " \"lido:term\" objects," +
                        " but EDM accepts only one license object.");
            }

            Term term = termList.get(0);
            String text = term.getText();

            List licences = Arrays.asList(Namespace.LICENSE_NAMES);
            int index = licences.indexOf(text);
            String licenseLink = Namespace.LICENSE_LINKS[index];

            return model.createResource(licenseLink);
        }

        return null;
    }
}
