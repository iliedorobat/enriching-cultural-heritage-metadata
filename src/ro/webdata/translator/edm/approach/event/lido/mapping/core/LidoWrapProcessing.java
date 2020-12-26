package ro.webdata.translator.edm.approach.event.lido.mapping.core;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.lido.core.ParserDAO;
import ro.webdata.parser.xml.lido.core.ParserDAOImpl;
import ro.webdata.parser.xml.lido.core.leaf.administrativeMetadata.AdministrativeMetadata;
import ro.webdata.parser.xml.lido.core.leaf.category.Category;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.parser.xml.lido.core.leaf.lido.Lido;
import ro.webdata.parser.xml.lido.core.leaf.lidoRecID.LidoRecID;
import ro.webdata.parser.xml.lido.core.wrap.lidoWrap.LidoWrap;
import ro.webdata.translator.commons.EnvConstants;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.administrativeMetadata.AdministrativeMetadataProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.category.CategoryProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.descriptiveMetadata.DescriptiveMetadataProcessing;
import ro.webdata.translator.edm.approach.event.lido.mapping.core.lidoRecID.LidoRecIDProcessing;

import java.util.ArrayList;

public class LidoWrapProcessing {
    private static ParserDAO parserDAO = new ParserDAOImpl();
    private static CategoryProcessing categoryProcessing = new CategoryProcessing();
    private static LidoRecIDProcessing lidoRecIDProcessing = new LidoRecIDProcessing();
    private static DescriptiveMetadataProcessing descriptiveMetadataProcessing = new DescriptiveMetadataProcessing();
    private static AdministrativeMetadataProcessing administrativeMetadataProcessing = new AdministrativeMetadataProcessing();

    public void processing(Model model, String fullPath) {
        Print.operation(Const.OPERATION_START, fullPath, EnvConstants.SHOULD_PRINT);
        LidoWrap lidoWrap = parserDAO.parseLidoFile(fullPath);

        //TODO: change in the LIDO Parser component the "getLido" method name to "getLidoList"
        for (int i = 0; i < lidoWrap.getLido().size(); i++) {
            Lido lido = lidoWrap.getLido().get(i);

            ArrayList<LidoRecID> lidoRecIDList = lido.getLidoRecID();
            Category category = lido.getCategory();
            ArrayList<AdministrativeMetadata> administrativeMetadataList = lido.getAdministrativeMetadata();
            ArrayList<DescriptiveMetadata> descriptiveMetadataList = lido.getDescriptiveMetadata();

            AdministrativeMetadata administrativeMetadata = administrativeMetadataList.size() > 0
                    ? administrativeMetadataList.get(0)
                    : null;
            DescriptiveMetadata descriptiveMetadata = descriptiveMetadataList.size() > 0
                    ? descriptiveMetadataList.get(0)
                    : null;

            Resource providedCHO = generateProvidedCHO(model, lidoRecIDList);
            administrativeMetadataProcessing.processing(
                    model, providedCHO, administrativeMetadata
            );
            categoryProcessing.processing(
                    model, providedCHO, category
            );
            descriptiveMetadataProcessing.processing(
                    model, providedCHO, descriptiveMetadata
            );
        }

        Print.operation(Const.OPERATION_END, fullPath, EnvConstants.SHOULD_PRINT);
    }

    private Resource generateProvidedCHO(Model model, ArrayList<LidoRecID> lidoRecIDList) {
        String recordId = lidoRecIDProcessing.getRecordId(lidoRecIDList);
        Resource providedCHO = model.createResource(Namespace.NS_REPO_RESOURCE_CHO + recordId);
        providedCHO.addProperty(RDF.type, EDM.ProvidedCHO);

        return providedCHO;
    }
}
