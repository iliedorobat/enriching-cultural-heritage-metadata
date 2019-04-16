package ro.webdata.lido.convert.edm.common;

import ro.webdata.lido.convert.edm.common.constants.Constants;

public class PrintMessages {
    public static void printOperation(String operation) {
        if (Constants.PRINT_OPERATION)
            System.out.println(operation);
    }

    public static void printOperation(String operation, String fullPath) {
        if (Constants.PRINT_OPERATION)
            System.out.println(operation + ": " + fullPath);
    }
}
