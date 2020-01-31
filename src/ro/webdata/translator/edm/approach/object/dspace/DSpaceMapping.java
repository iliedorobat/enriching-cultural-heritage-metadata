package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.vocabulary.RDF;
import ro.webdata.common.constants.FileConstants;
import ro.webdata.parser.xml.dspace.core.Parser;
import ro.webdata.parser.xml.dspace.core.attribute.record.IdentifierRecord;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.parser.xml.dspace.core.wrapper.dc.DcWrapper;
import ro.webdata.translator.edm.approach.event.lido.vocabulary.EDM;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.DcMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DSpaceMapping {
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
     * @param dSpacePath The DSpace directory path (E.g.: FileConstants.PATH_INPUT_DSPACE_DIR)
     */
    public static void dSpaceParser(Model model, String dSpacePath) {
        File dSpaceDirectory = new File(dSpacePath);
        File[] subDirectories = dSpaceDirectory.listFiles();

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                dSpaceItemParser(model, subDirectory);
            }
        }
    }

    private static void dSpaceItemParser(Model model, File subDirectory) {
        if (subDirectory.isDirectory()) {
            File[] files = subDirectory.listFiles();

            if (files != null) {
                dSpaceFilesParser(model, files);
            }
        }
    }

    private static void dSpaceFilesParser(Model model, File[] files) {
        for (File file : files) {
            dSpaceFileMapping(model, file);
        }
    }

    private static void dSpaceFileMapping(Model model, File file) {
        if (file.isFile()) {
            String fileName = file.getName();
            String extension = FilenameUtils.getExtension(fileName);

            if (extension.equals(FileConstants.FILE_EXTENSION_XML)) {
                DcWrapper dcWrapper = Parser.parseDcXmlFile(file.getPath());
                String schemaName = dcWrapper.getSchemaName();
                Resource providedCHO = model.createResource()
                        .addProperty(RDF.type, EDM.ProvidedCHO);
                HashMap<String, ArrayList<DcValue>> dcValueMap = dcWrapper.getDcValueMap();

                switch (schemaName) {
                    case "dc":
                        providedCHO = updateProvidedCHO(model, providedCHO, dcValueMap);
                        DcMapping.processing(model, providedCHO, dcValueMap);
                    default:
                        break;
                }

                //TODO: "europeana" schema
                System.out.println(fileName + " " + extension);
            }
        }
    }

    private static Resource updateProvidedCHO(Model model, Resource providedCHO, HashMap<String, ArrayList<DcValue>> dcValueMap) {
        ArrayList<DcValue> identifierList = dcValueMap.get(IdentifierRecord.ELEMENT);

        int index = 0;
        while (index < identifierList.size()) {
            DcValue dcValue = identifierList.get(index);
            String qualifier = dcValue.getQualifier().getValue().toLowerCase();

            if (qualifier.equals("uri")) {
                String uri = dcValue.getText();
                providedCHO = ResourceUtils.renameResource(providedCHO, uri);
            }

            index++;
        }

        return providedCHO;
    }
}
