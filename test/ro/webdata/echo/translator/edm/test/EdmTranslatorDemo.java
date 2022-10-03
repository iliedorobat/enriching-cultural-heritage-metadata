package ro.webdata.echo.translator.edm.test;

import ro.webdata.echo.translator.edm.test.approach.event.CimecDemo;
import ro.webdata.echo.translator.edm.test.approach.event.LidoDemo;
import ro.webdata.echo.translator.edm.test.approach.object.DSpaceDemo;

import static ro.webdata.echo.translator.commons.Env.*;

public class EdmTranslatorDemo {
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
                CimecDemo.run();
                break;
            case DATA_TYPE_LIDO:
                LidoDemo.run();
                break;
            case DATA_TYPE_DSPACE:
            default:
                break;
        }
    }

    private static void runObjectCentricApproach(String dataType) {
        switch (dataType) {
            case DATA_TYPE_DSPACE:
                DSpaceDemo.run();
                break;
            case DATA_TYPE_CIMEC:
            case DATA_TYPE_LIDO:
            default:
                break;
        }
    }
}
