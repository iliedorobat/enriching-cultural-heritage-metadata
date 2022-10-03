package ro.webdata.echo.translator.edm.test.approach.event;

import ro.webdata.echo.translator.commons.FileConst;
import ro.webdata.echo.translator.edm.approach.event.Lido;

public class LidoDemo {
    public static void run() {
        Lido.mapEntries(FileConst.FILE_NAME_DEMO);
    }
}
