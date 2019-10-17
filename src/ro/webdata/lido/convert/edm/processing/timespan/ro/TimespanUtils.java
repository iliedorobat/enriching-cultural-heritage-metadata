package ro.webdata.lido.convert.edm.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.YearIntervalModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.TimespanModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.imprecise.DatelessModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.imprecise.InaccurateYearModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.DateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.LongDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.ShortDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.*;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.LongDateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.imprecise.DatelessRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.imprecise.InaccurateYearRegex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimespanUtils {
    private static final String LEA = "lea";
    private static final String LEA_REPLACEMENT = "##LEA##";
    private static final String MIJ = "mij";
    private static final String MIJ_REPLACEMENT = "##HALF##";

    private static final String URL_SEPARATOR = "/";
    private static final String A_MATCHING_GROUP = "([ ]+a[ \\.]+)";
    private static final String P_MATCHING_GROUP = "([ ]+p[ \\.]+)";
    private static final String CHR_MATCHING_GROUP = "(chr)([ \\.]+|\\z)";

    /**
     * TODO: doc<br/>
     * In the matching process the first matched value need to be the interval type,
     * followed by the ordinal values, respecting the following order:
     * <ol>
     *     <li>Map every unknown value in order to clear the list by junk elements</li>
     *     <li>Map every date-like value</li>
     *     <li>Map every century and millennium age-like value</li>
     *     <li>Map every epoch-like value</li>
     * </ol>
     * @param filePath
     */
    public static void read(String filePath) {
        try {
            TimespanModel timespanModel;
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // used for testing
            String test = "după 29 aprilie 1616";
            test = "post 330-320 a.chr.";
            br = new BufferedReader(new StringReader(test));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String value = StringUtils.stripAccents(readLine);

                    timespanModel = new TimespanModel(value);
                    timespanModel = getMatchedValues(timespanModel, UnknownRegex.UNKNOWN, null);

                    timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_DMY_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_YMD_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_DMY_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, DateRegex.DATE_YMD_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, LongDateRegex.LONG_DATE_OPTIONS, null);

                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_ROMAN_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_ROMAN_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, AgeRegex.AGE_OPTIONS, null);
//
                    timespanModel = getMatchedValues(timespanModel, DatelessRegex.DATELESS, null);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER_INTERVAL, InaccurateYearModel.AFTER);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE_INTERVAL, InaccurateYearModel.BEFORE);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_INTERVAL, InaccurateYearModel.APPROXIMATE);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER, InaccurateYearModel.AFTER);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE, InaccurateYearModel.BEFORE);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_OPTIONS, InaccurateYearModel.APPROXIMATE);
//                    timespanModel.clearTimespanList();

                    if (timespanModel.getTimespanList().size() > 0)
                        System.out.println(timespanModel.getTimespanList());
                }
            }
        } catch (IOException e) {

        }
    }

    private static TimespanModel getMatchedValues(TimespanModel timespanModel, String regex, String flag) {
        String initialValue = timespanModel.getResidualValue()
                .replaceAll(TimespanRegex.AGE_BC, TimespanRegex.CHRISTUM_BC)
                .replaceAll(TimespanRegex.AGE_AD, TimespanRegex.CHRISTUM_AD);

        // Particular logic to add a space before and after the interval delimiter
        if (initialValue.equals("17 nov. 375-9 aug. 378 __BC__")) {
            initialValue = "17 nov. 375 - 9 aug. 378 __BC__";
        }

        ArrayList<String> matchedList = timespanModel.getTimespanList();
        String residualValue = initialValue
                .replaceAll(regex, Constants.EMPTY_VALUE_PLACEHOLDER);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initialValue);
//        System.out.println("initialValue: " + initialValue);

        while (matcher.find()) {
//            System.out.println("matcher: " + matcher.group());
            // String prepared = prepareTimePeriod(prepared, regex);
            //TODO: check
            String prepared = prepareDateTime(matcher.group(), regex, flag);
            prepared = prepareAges(prepared, regex, flag);
            prepared = prepareTimePeriod(prepared, regex, flag);
//            matchedList.add(prepared);
            matchedList.add("prepared: \"" + prepared + "\"     initialValue: \"" + initialValue + "\"");
        }

        return new TimespanModel(matchedList, residualValue.trim());
    }

    //TODO: consolidate prepareDateTime & prepareAges & prepareTimePeriod
    private static String prepareValue(String value, String regex) {
        return null;
    }

    /**
     * If the value is a date-like, format it using a unique standard (year/month/day),
     * else, return the original value
     * @param value The original value
     * @param regex A regular expression used for matching
     * @param flag An optional value used to differentiate some cases
     * @return The formatted value
     */
    private static String prepareDateTime(String value, String regex, String flag) {
        DateModel dateModel;
        ShortDateModel shortDateModel;
        LongDateModel longDateModel;

        switch (regex) {
            case DateRegex.DATE_DMY_INTERVAL:
            case DateRegex.DATE_DMY_OPTIONS:
                dateModel = new DateModel(value, DateModel.DMY);
                return dateModel.toString();
            case DateRegex.DATE_YMD_INTERVAL:
            case DateRegex.DATE_YMD_OPTIONS:
                dateModel = new DateModel(value, DateModel.YMD);
                return dateModel.toString();
            case ShortDateRegex.DATE_MY_INTERVAL:
            case ShortDateRegex.DATE_MY_OPTIONS:
                shortDateModel = new ShortDateModel(value, ShortDateModel.MY);
                return shortDateModel.toString();
            case LongDateRegex.LONG_DATE_OPTIONS:
                longDateModel = new LongDateModel(value);
                return longDateModel.toString();
            default:
                return value;
        }
    }

    private static String prepareAges(String value, String regex, String flag) {
        YearIntervalModel yearIntervalModel;
        DatelessModel datelessModel;
        InaccurateYearModel inaccurateYearModel;

        switch (regex) {
//            case TimespanRegex.AGES_4_INTERVAL:
//            case TimespanRegex.AGES_1_3_INTERVAL:
//            case TimespanRegex.AGES_APPROX_INTERVAL:
//                dateIntervalModel = new DateIntervalModel(value);
//                return dateIntervalModel.toString();
//            case TimespanRegex.AGES_APPROX:
//            case TimespanRegex.AGES_1_4_UNCERTAIN:
//            case TimespanRegex.AGES_1_4_CHRISTUM_GROUP:
            case InaccurateYearRegex.AFTER_INTERVAL:
            case InaccurateYearRegex.BEFORE_INTERVAL:
            case InaccurateYearRegex.APPROX_AGES_INTERVAL:
            case InaccurateYearRegex.AFTER:
            case InaccurateYearRegex.BEFORE:
            case InaccurateYearRegex.APPROX_AGES_OPTIONS:
                inaccurateYearModel = new InaccurateYearModel(value, flag);
                return inaccurateYearModel.toString();
            case DatelessRegex.DATELESS:
                datelessModel = new DatelessModel(value);
                return datelessModel.toString();
            default:
                return value;
        }
    }

    private static String prepareTimePeriod(String value, String regex, String flag) {
        switch (regex) {
//            case TimespanRegex.CENTURY_INTERVAL:
//            case TimespanRegex.CENTURY_OPTIONS:
//            case TimespanRegex.MILLENNIUM_INTERVAL:
//            case TimespanRegex.MILLENNIUM_OPTIONS:
//            case TimePeriodRegex.OTHER_ROMAN_INTERVAL:
//            case TimePeriodRegex.OTHER_ROMAN_OPTIONS:
//                return value
//                        .replaceAll(TimespanRegex.AGE_AD, TimespanRegex.CHRISTUM_AD)
//                        .replaceAll(TimespanRegex.AGE_BC, TimespanRegex.CHRISTUM_BC);
//                return value.replaceAll(MIJ_REPLACEMENT, MIJ);
            case AgeRegex.AGE_OPTIONS:
            case UnknownRegex.UNKNOWN:
            default:
                return value;
        }
    }


    /**
     * Map the prepared era value to simple name.
     * The prepared era value is the original era value which
     * has been processed as following:
     * <ul>
     *     <li>era that is matching with TimespanRegex.CHRISTUM_AD will be mapped to "AD"</li>
     *     <li>era that is matching with TimespanRegex.CHRISTUM_BC will be mapped to "BC"</li>
     * </ul>
     * @param value The input value
     * @return "AD" ord "BC"
     */
    public static String getEraName(String value) {
        return value.contains(TimespanRegex.CHRISTUM_BC)
                ? "BC"
                : "AD";
    }

    /**
     * Extract the era from the prepared value.
     * The prepared value is the original value for which
     * era has been processed as following:
     * <ul>
     *     <li>all values that are matching with TimespanRegex.AGE_AD will be mapped to TimespanRegex.CHRISTUM_AD</li>
     *     <li>all values that are matching with TimespanRegex.AGE_BC will be mapped to TimespanRegex.CHRISTUM_BC</li>
     * </ul>
     * @param value The input value
     * @return "__AD__" or "__BC__"
     */
    public static String getEraPlaceholder(String value) {
        return value.contains(TimespanRegex.CHRISTUM_BC)
                ? TimespanRegex.CHRISTUM_BC
                : TimespanRegex.CHRISTUM_AD;
    }

    /**
     * Remove the Christum notation ("__AD__" and "__BC__")
     * @param value The input value
     * @return The value without Christum notation
     */
    public static String clearChristumNotation(String value) {
        return value
                .replaceAll(TimespanRegex.CHRISTUM_BC, Constants.EMPTY_VALUE_PLACEHOLDER)
                .replaceAll(TimespanRegex.CHRISTUM_AD, Constants.EMPTY_VALUE_PLACEHOLDER)
                .trim();
    }
}
