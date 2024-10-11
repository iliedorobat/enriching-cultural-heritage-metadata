package ro.webdata.echo.demo.translator.edm;

import static ro.webdata.echo.translator.commons.Env.*;

public class EdmTranslatorDemo {
    public static void run(String dataType) {
        if (dataType != null) {
            switch (dataType) {
                case DATA_TYPE_CIMEC:
                    CimecDemo.run();
                    break;
                case DATA_TYPE_LIDO:
                    LidoDemo.run();
                    break;
                case DATA_TYPE_DSPACE:
                    DSpaceDemo.run();
                default:
                    break;
            }
        }
    }
}
