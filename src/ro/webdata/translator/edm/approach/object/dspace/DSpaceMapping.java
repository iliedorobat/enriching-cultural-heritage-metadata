package ro.webdata.translator.edm.approach.object.dspace;

import org.apache.commons.io.FilenameUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import ro.webdata.echo.commons.schema.Namespace;
import ro.webdata.parser.xml.dspace.core.Parser;
import ro.webdata.parser.xml.dspace.core.leaf.dcValue.DcValue;
import ro.webdata.parser.xml.dspace.core.wrapper.dc.DcWrapper;
import ro.webdata.translator.edm.approach.object.dspace.commons.ProvidedCHO;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.dc.DcMapping;
import ro.webdata.translator.edm.approach.object.dspace.mapping.core.europeana.EuropeanaMapping;

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
     * @param model The RDF graph
     * @param dSpacePath The DSpace directory path (E.g.: FileConstants.PATH_INPUT_DSPACE_DIR)
     */
    public static void processing(Model model, String dSpacePath) {
        File dSpaceDirectory = new File(dSpacePath);
        File[] subDirectories = dSpaceDirectory.listFiles();
        Resource providedCHO = ProvidedCHO.dSpaceFileMapping(model, dSpacePath);

        if (subDirectories != null) {
            for (File subDirectory : subDirectories) {
                itemProcessing(model, providedCHO, subDirectory);
            }
        }
    }

    private static void itemProcessing(Model model, Resource providedCHO, File subDirectory) {
        if (subDirectory.isDirectory()) {
            File[] files = subDirectory.listFiles();

            if (files != null) {
                filesProcessing(model, providedCHO, files);
            }
            // TODO: check if edm:type has been added; if not, extract the value from the file extension
        }
    }

    private static void filesProcessing(Model model, Resource providedCHO, File[] files) {
        for (File file : files) {
            fileProcessing(model, providedCHO, file);
        }
    }

    private static void fileProcessing(Model model, Resource providedCHO, File file) {
        if (file.isFile()) {
            String fileName = file.getName();
            String extension = FilenameUtils.getExtension(fileName);

            if (extension.equals(ro.webdata.echo.commons.File.EXTENSION_XML)) {
                DcWrapper dcWrapper = Parser.parseDcXmlFile(file.getPath());
                String schemaName = dcWrapper.getSchemaName();
                HashMap<String, ArrayList<DcValue>> dcValueMap = dcWrapper.getDcValueMap();

                switch (schemaName) {
                    case Namespace.DC:
                        DcMapping.processing(model, providedCHO, dcValueMap);
                        break;
                    case Namespace.EUROPEANA:
                        EuropeanaMapping.processing(model, providedCHO, dcValueMap);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
