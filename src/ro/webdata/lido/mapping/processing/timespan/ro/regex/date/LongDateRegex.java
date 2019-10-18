package ro.webdata.lido.mapping.processing.timespan.ro.regex.date;

public class LongDateRegex {
    public static final String DATE_SEPARATOR = ";";
    public static final String LONG_DATE_OPTIONS = "^"
                + "s:[\\d]{1,2}"
                + DATE_SEPARATOR + "a:[\\d]{1,4}"
                + DATE_SEPARATOR +"l:[\\d]{1,2}"
                + DATE_SEPARATOR +"z:[\\d]{1,2}"
            + "$";
}
