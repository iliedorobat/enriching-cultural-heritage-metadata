package ro.webdata.translator.edm.approach.event.lido.mapping.leaf;

import ro.webdata.echo.commons.File;
import ro.webdata.parser.xml.lido.core.leaf.recordID.RecordID;
import ro.webdata.parser.xml.lido.core.wrap.recordWrap.RecordWrap;

import java.util.ArrayList;

public class RecordIDProcessing {
    /**
     * Generate an unique identifier that would be used to identify all the resources related to one CHO
     * @param recordWrap The <b>lido:recordWrap</b> element
     * @return An unique identifier
     */
    public static String consolidatesIdentifiers(RecordWrap recordWrap) {
        ArrayList<RecordID> recordIDList = recordWrap.getRecordID();
        return consolidatesIdentifiers(recordIDList);
    }

    /**
     * Generate an unique identifier that would be used to identify all the resources related to one CHO
     * @param idList The LIDO list with <b>lido:recordID</b> elements
     * @return An unique identifier
     */
    //TODO: is similar with LidoRecIDProcessing.getRecordId
    public static String consolidatesIdentifiers(ArrayList<RecordID> idList) {
        String id = null;

        if (idList.size() > 0) {
            RecordID recordID = idList.get(0);
            String text = recordID.getText();
            String type = recordID.getType().getType();

            if (type != null) {
                id = File.FILE_SEPARATOR + type + File.FILE_SEPARATOR + text;
            } else {
                id = File.FILE_SEPARATOR + text;
            }
        } else {
            System.err.println(RecordIDProcessing.class.getName() + ":" +
                    "There should be provided at least one \"lido:recordID\" property" +
                    "but no any \"lido:recordID\" has been identified.");
        }

        return id;
    }
}
