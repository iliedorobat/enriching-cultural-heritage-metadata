package ro.webdata.echo.translator.edm.lido.mapping.core;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.Print;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.echo.translator.edm.lido.commons.URIUtils;
import ro.webdata.echo.translator.edm.lido.mapping.core.administrativeMetadata.AdministrativeMetadataProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.core.category.CategoryProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.core.descriptiveMetadata.DescriptiveMetadataProcessing;
import ro.webdata.echo.translator.edm.lido.mapping.core.lidoRecID.LidoRecIDProcessing;
import ro.webdata.parser.xml.lido.core.ParserDAO;
import ro.webdata.parser.xml.lido.core.ParserDAOImpl;
import ro.webdata.parser.xml.lido.core.leaf.administrativeMetadata.AdministrativeMetadata;
import ro.webdata.parser.xml.lido.core.leaf.category.Category;
import ro.webdata.parser.xml.lido.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.parser.xml.lido.core.leaf.lido.Lido;
import ro.webdata.parser.xml.lido.core.leaf.lidoRecID.LidoRecID;
import ro.webdata.parser.xml.lido.core.wrap.lidoWrap.LidoWrap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static ro.webdata.echo.commons.graph.Namespace.NS_REPO_RESOURCE_CHO;
import static ro.webdata.echo.translator.commons.Env.IS_PRINT_ENABLED;

public class LidoWrapProcessing {
    private static final ParserDAO parserDAO = new ParserDAOImpl();

    public static void mapEntries(Model model, String fullPath) {
        Print.operation(Const.OPERATION_START, fullPath, IS_PRINT_ENABLED);

        LidoWrap lidoWrap = parserDAO.parseLidoFile(fullPath);
        ArrayList<Lido> lidoList = lidoWrap.getLidoList();

        int i = 0;
        for (Lido lido : lidoList) {
            ArrayList<LidoRecID> lidoRecIDList = lido.getLidoRecID();
            if (lidoRecIDList.size() > 0)
                System.out.println(
                        ++i
                        + "   " + new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new java.util.Date())
                        + "   " + lidoRecIDList.get(0).getText()
                );

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
            AdministrativeMetadataProcessing.mapEntries(
                    model, providedCHO, administrativeMetadata
            );
            CategoryProcessing.mapEntries(
                    model, providedCHO, category
            );
            DescriptiveMetadataProcessing.mapEntries(
                    model, providedCHO, descriptiveMetadata
            );
        }

        Print.operation(Const.OPERATION_END, fullPath, IS_PRINT_ENABLED);
    }

    private static Resource generateProvidedCHO(Model model, ArrayList<LidoRecID> lidoRecIDList) {
        String recordId = LidoRecIDProcessing.getRecordId(lidoRecIDList);
        String uri = URIUtils.prepareUri(NS_REPO_RESOURCE_CHO, recordId);

        Resource providedCHO = model.createResource(uri);
        providedCHO.addProperty(RDF.type, EDM.ProvidedCHO);

        return providedCHO;
    }
}
