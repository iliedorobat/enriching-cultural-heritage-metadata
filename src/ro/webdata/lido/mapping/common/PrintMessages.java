package ro.webdata.lido.mapping.common;

import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.common.constants.EnvConst;

public class PrintMessages {
    public static void printOperation(String operation) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation);
    }

    public static void printOperation(String operation, String fullPath) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation + ": " + fullPath);
    }

    public static void printTooBigYear(int year) {
        System.err.println("The year " + year + " is higher than the last year " +
                "the data was updated (" + Constants.LAST_UPDATE_YEAR + ")!");
    }

    public static void printUnknownMonth(int monthNumber, String monthName) {
        System.err.println("Invalid month number: " + monthNumber
                + " (month name: \"" + monthName + "\")!");
    }
}
