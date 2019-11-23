package ro.webdata.lido.translator.common;

import ro.webdata.lido.translator.common.constants.Constants;
import ro.webdata.lido.translator.common.constants.EnvConst;

public class PrintMessages {
    public static void printOperation(String operation) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation);
    }

    public static void printOperation(String operation, String fullPath) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation + ": " + fullPath);
    }

    public static void printTooBigYear(String operation, String position, int year) {
        System.err.println(operation + ": " + "The " + position + " year " + year
                + " is higher than the last year the data was updated ("
                + Constants.LAST_UPDATE_YEAR + ")!");
    }

    public static void printUnknownMonth(int monthNumber, String monthName) {
        System.err.println("Invalid month number: " + monthNumber
                + " (month name: \"" + monthName + "\")!");
    }
}
