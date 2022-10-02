package ro.webdata.translator.edm;

import ro.webdata.translator.edm.approach.event.Cimec;
import ro.webdata.translator.edm.approach.event.Lido;
import ro.webdata.translator.edm.approach.object.DSpace;

import static ro.webdata.translator.commons.Env.*;

// TODO: ro.webdata.echo.translator....
public class EdmTranslator {
    public static void run(String approach, String dataType) {
        if (approach != null && dataType != null) {
            if (approach.equals(EDM_APPROACH_EVENT_CENTRIC)) {
                runEventCentricApproach(dataType);
            } else if (approach.equals(EDM_APPROACH_OBJECT_CENTRIC)) {
                runObjectCentricApproach(dataType);
            }
        }
    }

    private static void runEventCentricApproach(String dataType) {
        switch (dataType) {
            case DATA_TYPE_CIMEC:
                Cimec.run();
                break;
            case DATA_TYPE_LIDO:
                Lido.run();
                break;
            case DATA_TYPE_DSPACE:
            default:
                break;
        }
    }

    private static void runObjectCentricApproach(String dataType) {
        switch (dataType) {
            case DATA_TYPE_DSPACE:
                DSpace.run();
                break;
            case DATA_TYPE_CIMEC:
            case DATA_TYPE_LIDO:
            default:
                break;
        }
    }
}
