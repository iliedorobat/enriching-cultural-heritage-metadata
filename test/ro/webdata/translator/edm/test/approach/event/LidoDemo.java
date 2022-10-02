package ro.webdata.translator.edm.test.approach.event;

import ro.webdata.translator.commons.FileConst;
import ro.webdata.translator.edm.approach.event.Lido;

public class LidoDemo {
    public static void run() {
        Lido.mapEntries(FileConst.FILE_NAME_DEMO);
    }
}
