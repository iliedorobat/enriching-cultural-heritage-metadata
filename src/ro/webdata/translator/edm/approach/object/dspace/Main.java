package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.parser.xml.dspace.core.Parser;
import ro.webdata.parser.xml.dspace.core.attribute.record.IdentifierRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.parser.xml.dspace.core.wrapper.dc.DcWrapper;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.ORE;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.FileConstants;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.DcValueMapping;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    private static final String SYNTAX = "RDF/XML";
    private static final String[] PATH_LIST = {
//            FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR + FileConstants.FILE_NAME_DEMO + FileConstants.XML_FILE_EXTENSION,
            FileConstants.PATH_INPUT_DSPACE_DIR
                    + FileConstants.FILE_SEPARATOR + "item_133703"
                    + FileConstants.FILE_SEPARATOR + "dublin_core" + FileConstants.FILE_EXTENSION_XML
    };

    public static void main(String[] args) {
        Model model = generateModel();

        //TODO:
//        File dspaceDirectory = new File(FileConstants.FILE_PATH_DSPACE);
//        File[] directories = dspaceDirectory.listFiles();
//        for (File directory : directories) {
//            if (directory.isDirectory()) {
//                File[] files = directory.listFiles();
//
//                for (File file : files) {
//                    if (file.isFile()) {
//                        System.out.println(file.getName());
//                    }
//                }
//            }
//        }

        for (String path : PATH_LIST) {
            DcWrapper dcWrapper = Parser.parseDcXmlFile(path);
            HashMap<String, ArrayList<DcValue>> dcValueMap = dcWrapper.getDcValueMap();
            Resource providedCHO = generateProvidedCHO(model, dcValueMap);
            DcValueMapping.processing(model, providedCHO, dcValueMap);
        }

        writeRDFGraph(model, null);
    }

    private static Model generateModel() {
        return ModelFactory.createDefaultModel()
                .setNsPrefix("dc", DC_11.getURI())
                .setNsPrefix("dcterms", DCTerms.getURI())
                .setNsPrefix("edm", EDM.getURI())
                .setNsPrefix("foaf", FOAF.getURI())
                .setNsPrefix("ore", ORE.getURI())
                .setNsPrefix("skos", SKOS.getURI())
                .setNsPrefix("openData", NSConstants.NS_REPO_PROPERTY + FileConstants.FILE_SEPARATOR);
    }

    private static Resource generateProvidedCHO(Model model, HashMap<String, ArrayList<DcValue>> dcValueMap) {
        Resource providedCHO = null;
        ArrayList<DcValue> identifierList = dcValueMap.get(IdentifierRecord.ELEMENT);

        int index = 0;
        while (providedCHO == null && index < identifierList.size()) {
            DcValue dcValue = identifierList.get(index);
            String qualifier = dcValue.getQualifier().getValue().toLowerCase();

            if (qualifier.equals("uri")) {
                String uri = dcValue.getText();
                providedCHO = model.createResource(uri);
                providedCHO.addProperty(RDF.type, EDM.ProvidedCHO);
            }

            index++;
        }

        return providedCHO;
    }

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, SYNTAX);
//        FileUtils.write(writer, outputFilePath);
        String result = writer.toString();
        System.out.println(result);
    }
}
