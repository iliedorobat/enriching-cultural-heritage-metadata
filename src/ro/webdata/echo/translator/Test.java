package ro.webdata.echo.translator;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.graph.GraphModel;
import ro.webdata.echo.translator.commons.GraphUtils;
import ro.webdata.echo.translator.edm.lido.commons.FileUtils;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;

import static ro.webdata.echo.translator.commons.Env.PRINT_RDF_RESULTS;

public class Test {
    private static String DBR = "https://dbpedia.org/resource/";

    private static final Model M_MODEL = ModelFactory.createDefaultModel();
    private static final Property fullName = M_MODEL.createProperty("https://dbpedia.org/property/fullName");
    private static final Property title = M_MODEL.createProperty("https://dbpedia.org/property/title");
    private static final Property spouse = M_MODEL.createProperty("https://dbpedia.org/ontology/spouse");

    public static void main(String[] args) {
        Model model = GraphModel.generateModel();
        String outputPath = FileUtils.getOutputFilePath("test");

        Resource kingOfRomania = generateResource(model, "King_of_Romania", OWL2.Thing);
        Resource ferdinand = generateResource(model, "Ferdinand_I_of_Romania", FOAF.Person);
        Resource carol = generateResource(model, "Carol_I_of_Romania", FOAF.Person);
        Resource elisabeth = generateResource(model, "Elisabeth_of_Wied", FOAF.Person);

        ferdinand.addProperty(fullName, "Ferdinand Viktor Albert Meinrad");
        ferdinand.addProperty(title, kingOfRomania);
        carol.addProperty(title, kingOfRomania);
        elisabeth.addProperty(spouse, carol);

        GraphUtils.writeRDFGraph(model, outputPath, PRINT_RDF_RESULTS);
    }

    private static Resource generateResource(Model model, String name, Resource type) {
        Resource resStefan = model.createResource(
                URIUtils.prepareUri(DBR, name)
        );
        resStefan.addProperty(RDF.type, type);

        return resStefan;
    }
}
