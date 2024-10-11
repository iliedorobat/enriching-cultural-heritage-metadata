package ro.webdata.echo.demo.translator.edm;

import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.lido.LidoTranslator;

public class LidoDemo {
    public static void run() {
        LidoTranslator.mapEntries(FileConst.FILE_NAME_DEMO);
    }
}
