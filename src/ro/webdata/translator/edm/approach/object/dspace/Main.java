package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DC_11;
import org.apache.jena.vocabulary.SKOS;
import ro.webdata.translator.edm.approach.event.lido.common.constants.NSConstants;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.ORE;
import ro.webdata.translator.edm.approach.object.dspace.common.constants.FileConstants;

import java.io.StringWriter;

public class Main {
    private static final String SYNTAX = "RDF/XML";

    //TODO: EDM.type => EDMConstants.EDM_TYPES
    public static void main(String[] args) {
        Model model = generateModel();
        DSpaceMapping.dSpaceParser(model, FileConstants.PATH_INPUT_DSPACE_DIR);
//        writeRDFGraph(model, null);
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

    private static void writeRDFGraph(Model model, String outputFilePath) {
        StringWriter writer = new StringWriter();
        model.write(writer, SYNTAX);
//        FileUtils.write(writer, outputFilePath);
        String result = writer.toString();
        System.out.println(result);
    }
}
