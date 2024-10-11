package ro.webdata.echo.translator.edm.lido.mapping.leaf;

import ro.webdata.echo.commons.graph.Namespace;
import ro.webdata.parser.xml.lido.core.leaf.recordID.RecordID;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;

import java.util.ArrayList;

public class RecordIDProcessing {
    /**
     * Generate a unique identifier that would be used to identify all the resources related to one CHO
     * @param recordWrap The <b>lido:recordWrap</b> element
     * @return An unique identifier
     */
    public static String getRecordId(RecordWrap recordWrap) {
        ArrayList<RecordID> recordIDList = recordWrap.getRecordID();
        return getRecordId(recordIDList);
    }

    /**
     * Generate a unique identifier that would be used to identify all the resources related to one CHO
     * @param idList The LIDO list with <b>lido:recordID</b> elements
     * @return An unique identifier
     */
    public static String getRecordId(ArrayList<RecordID> idList) {
        String id = null;

        if (idList.size() > 0) {
            RecordID recordID = idList.get(0);
            String text = recordID.getText();
            String type = recordID.getType().getType();

            if (type != null) {
                id = Namespace.URL_SEPARATOR + type + Namespace.URL_SEPARATOR + text;
            } else {
                id = Namespace.URL_SEPARATOR + text;
            }
        } else {
            System.err.println(RecordIDProcessing.class.getName() + ":" +
                    "There should be provided at least one \"lido:recordID\" property" +
                    "but no any \"lido:recordID\" has been identified.");
        }

        return id;
    }
}
