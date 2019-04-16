package ro.webdata.lido.convert.edm.core.lidoRecID;

import ro.webdata.lido.convert.edm.common.constants.FileConstatnts;
import ro.webdata.lido.parser.core.leaf.lidoRecID.LidoRecID;

import java.util.ArrayList;

public class LidoRecIDProcessing {
    /**
     * Generate an unique identifier that would be used to identify all the resources related to one CHO
     * @param lidoRecIDList The LIDO list with lido lidoRecID elements
     * @return An unique identifier
     */
    //TODO: is similar with RecordIDProcessing.consolidatesIdentifiers
    public String getRecordId(ArrayList<LidoRecID> lidoRecIDList) {
        String id = null;

        if (lidoRecIDList.size() > 0) {
            LidoRecID lidoRecID = lidoRecIDList.get(0);
            String text = lidoRecID.getText();
            String type = lidoRecID.getType().getType();

            if (type != null)
                id = FileConstatnts.FILE_SEPARATOR + type + FileConstatnts.FILE_SEPARATOR + text;
            else
                id = FileConstatnts.FILE_SEPARATOR + text;
        } else {
            System.err.println(this.getClass().getName() + ":" +
                    "There should be provided at least one \"lido:lidoRecID\" property" +
                    "but no any \"lido:lidoRecID\" has been identified.");
        }

        return id;
    }
}
