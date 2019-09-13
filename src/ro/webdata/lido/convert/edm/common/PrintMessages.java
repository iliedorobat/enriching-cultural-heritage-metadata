package ro.webdata.lido.convert.edm.common;

import ro.webdata.lido.convert.edm.common.constants.EnvConst;

public class PrintMessages {
    public static void printOperation(String operation) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation);
    }

    public static void printOperation(String operation, String fullPath) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation + ": " + fullPath);
    }
}
