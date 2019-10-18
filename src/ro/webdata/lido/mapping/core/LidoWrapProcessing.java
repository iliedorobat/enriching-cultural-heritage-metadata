package ro.webdata.lido.mapping.core;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.lido.mapping.common.PrintMessages;
import ro.webdata.lido.mapping.common.constants.EnvConst;
import ro.webdata.lido.mapping.common.constants.NSConstants;
import ro.webdata.lido.mapping.core.administrativeMetadata.AdministrativeMetadataProcessing;
import ro.webdata.lido.mapping.core.category.CategoryProcessing;
import ro.webdata.lido.mapping.core.descriptiveMetadata.DescriptiveMetadataProcessing;
import ro.webdata.lido.mapping.core.lidoRecID.LidoRecIDProcessing;
import ro.webdata.lido.mapping.vocabulary.EDM;
import ro.webdata.lido.parser.core.ParserDAO;
import ro.webdata.lido.parser.core.ParserDAOImpl;
import ro.webdata.lido.parser.core.leaf.administrativeMetadata.AdministrativeMetadata;
import ro.webdata.lido.parser.core.leaf.category.Category;
import ro.webdata.lido.parser.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.lido.parser.core.leaf.lido.Lido;
import ro.webdata.lido.parser.core.leaf.lidoRecID.LidoRecID;
import ro.webdata.lido.parser.core.wrap.lidoWrap.LidoWrap;

import java.util.ArrayList;

public class LidoWrapProcessing {
    private static ParserDAO parserDAO = new ParserDAOImpl();
    private static CategoryProcessing categoryProcessing = new CategoryProcessing();
    private static LidoRecIDProcessing lidoRecIDProcessing = new LidoRecIDProcessing();
    private static DescriptiveMetadataProcessing descriptiveMetadataProcessing = new DescriptiveMetadataProcessing();
    private static AdministrativeMetadataProcessing administrativeMetadataProcessing = new AdministrativeMetadataProcessing();

    public void processing(Model model, String fullPath) {
        PrintMessages.printOperation(EnvConst.OPERATION_START, fullPath);
        LidoWrap lidoWrap = parserDAO.parseLidoFile(fullPath);

        //TODO: change in the parser module from "getLido" to "getLidoList"
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

        PrintMessages.printOperation(EnvConst.OPERATION_END, fullPath);
    }

    private Resource generateProvidedCHO(Model model, ArrayList<LidoRecID> lidoRecIDList) {
        String recordId = lidoRecIDProcessing.getRecordId(lidoRecIDList);
        Resource providedCHO = model.createResource(NSConstants.NS_REPO_RESOURCE_CHO + recordId);
        providedCHO.addProperty(RDF.type, EDM.ProvidedCHO);

        return providedCHO;
    }
}
