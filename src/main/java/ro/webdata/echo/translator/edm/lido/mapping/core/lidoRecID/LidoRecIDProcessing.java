package ro.webdata.echo.translator.edm.lido.mapping.core.lidoRecID;

import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.parser.xml.lido.core.leaf.lidoRecID.LidoRecID;

import java.util.ArrayList;

public class LidoRecIDProcessing {
    /**
     * Generate a unique identifier that would be used to identify all the resources related to one CHO
     * @param lidoRecIDList The LIDO list with lido lidoRecID elements
     * @return An unique identifier
     */
    // TODO: consolidate with RecordIDProcessing.getRecordId
    public static String getRecordId(ArrayList<LidoRecID> lidoRecIDList) {
        String id = null;

        if (lidoRecIDList.size() > 0) {
            LidoRecID lidoRecID = lidoRecIDList.get(0);
            String text = lidoRecID.getText();
            String type = lidoRecID.getType().getType();

            if (type != null) {
                id = Namespace.URL_SEPARATOR + type + Namespace.URL_SEPARATOR + text;
            } else {
                id = Namespace.URL_SEPARATOR + text;
            }
        } else {
            System.err.println(LidoRecIDProcessing.class.getName() + ":" +
                    "There should be provided at least one \"lido:lidoRecID\" property" +
                    "but no any \"lido:lidoRecID\" has been identified.");
        }

        return id;
    }
}
