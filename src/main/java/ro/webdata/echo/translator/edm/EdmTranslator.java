package ro.webdata.echo.translator.edm;

import ro.webdata.echo.translator.edm.cimec.CimecTranslator;
import ro.webdata.echo.translator.edm.dspace.DSpaceTranslator;
import ro.webdata.echo.translator.edm.lido.LidoTranslator;

import static ro.webdata.echo.translator.commons.Env.*;

public class EdmTranslator {
    public static void run(String dataType) {
        if (dataType != null) {
            switch (dataType) {
                case DATA_TYPE_CIMEC:
                    CimecTranslator.run();
                    break;
                case DATA_TYPE_LIDO:
                    LidoTranslator.run();
                    break;
                case DATA_TYPE_DSPACE:
                    DSpaceTranslator.run();
                default:
                    break;
            }
        }
    }
}
