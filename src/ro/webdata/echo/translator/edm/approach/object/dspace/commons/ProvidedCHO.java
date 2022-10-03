package ro.webdata.echo.translator.edm.approach.object.dspace.commons;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.echo.commons.graph.vocab.EDM;
import ro.webdata.parser.xml.dspace.core.Parser;
import ro.webdata.parser.xml.dspace.core.attribute.record.IdentifierRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.parser.xml.dspace.core.wrapper.dc.DcWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class ProvidedCHO {
    /**
     * Example of DSpace directory:
     * main_directory/
     *              item1/
     *                  dublin_core.xml
     *                  contents
     *                  contentFile1.ext
     *                  contentFile2.ext
     *                  ...
     *                  contentFileM.ext
     *              item2/
     *              ...
     *              itemN/
     * @param model The RDF graph
     * @param dSpacePath The DSpace directory path (E.g.: FileConstants.PATH_INPUT_DSPACE_DIR)
     */
    public static Resource dSpaceFileMapping(Model model, String dSpacePath) {
        Resource providedCHO = null;
        File dSpaceDirectory = new File(dSpacePath);
        File[] subDirectories = dSpaceDirectory.listFiles();

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                providedCHO = dSpaceItemParser(model, subDirectory);
            }
        }

        return providedCHO;
    }

    private static Resource dSpaceItemParser(Model model, File subDirectory) {
        Resource providedCHO = null;

        if (subDirectory.isDirectory()) {
            File[] files = subDirectory.listFiles();

            if (files != null) {
                providedCHO = dSpaceFilesParser(model, files);
            }
        }

        return providedCHO;
    }

    private static Resource dSpaceFilesParser(Model model, File[] files) {
        Resource providedCHO = null;

        int index = 0;
        while (index < files.length && providedCHO == null) {
            providedCHO = dSpaceFileMapping(model, files[index]);
            index++;
        }

        return providedCHO;
    }

    private static Resource dSpaceFileMapping(Model model, File file) {
        Resource providedCHO = null;

        if (file.isFile()) {
            String fileName = file.getName();
            String extension = FilenameUtils.getExtension(fileName);

            if (extension.equals(ro.webdata.echo.commons.File.EXTENSION_XML)) {
                DcWrapper dcWrapper = Parser.parseDcXmlFile(file.getPath());
                String schemaName = dcWrapper.getSchemaName();

                // TODO: use a constant
                if (schemaName.equals("dc")) {
                    providedCHO = generateProvidedCHO(model, dcWrapper);
                }
            }
        }

        return providedCHO;
    }

    private static Resource generateProvidedCHO(Model model, DcWrapper dcWrapper) {
        Resource providedCHO = null;
        HashMap<String, ArrayList<DcValue>> dcValueMap = dcWrapper.getDcValueMap();
        ArrayList<DcValue> identifierList = dcValueMap.get(IdentifierRecord.ELEMENT);

        int index = 0;
        while (index < identifierList.size()) {
            DcValue dcValue = identifierList.get(index);
            String qualifier = dcValue.getQualifier().getValue().toLowerCase();

            if (qualifier.equals("uri")) {
                String uri = dcValue.getText();
                providedCHO = model
                        .createResource(uri)
                        .addProperty(RDF.type, EDM.ProvidedCHO);
            }

            index++;
        }

        return providedCHO;
    }
}
