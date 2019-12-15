package ro.webdata.translator.edm.approach.event.lido.common;

import ro.webdata.translator.edm.approach.event.lido.common.constants.Constants;
import ro.webdata.translator.edm.approach.event.lido.common.constants.EnvConst;

public class PrintMessages {
    public static void printOperation(String operation) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation);
    }

    public static void printOperation(String operation, String fullPath) {
        if (EnvConst.PRINT_OPERATION)
            System.out.println(operation + ": " + fullPath);
    }

    public static void printTooBigCentury(String operation, String position, int century) {
        System.err.println(operation + ": " + "The " + position + " century " + century
                + " is higher than the last century the data has been updated ("
                + Constants.LAST_UPDATE_CENTURY + ")!");
    }

    public static void printTooBigMillennium(String operation, String position, int millennium) {
        System.err.println(operation + ": " + "The " + position + " millennium " + millennium
                + " is higher than the last millennium the data has been updated ("
                + Constants.LAST_UPDATE_MILLENNIUM + ")!");
    }

    public static void printTooBigYear(String operation, String position, int year) {
        System.err.println(operation + ": " + "The " + position + " year " + year
                + " is higher than the last year the data has been updated ("
                + Constants.LAST_UPDATE_YEAR + ")!");
    }

    public static void printUnknownMonth(int monthNumber, String monthName) {
        System.err.println("Invalid month number: " + monthNumber
                + " (month name: \"" + monthName + "\")!");
    }
}
