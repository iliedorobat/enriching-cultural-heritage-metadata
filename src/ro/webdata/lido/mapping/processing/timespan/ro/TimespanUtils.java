package ro.webdata.lido.mapping.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.lido.mapping.common.constants.Constants;
import ro.webdata.lido.mapping.processing.timespan.ro.model.TimespanModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.YearModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.date.DateModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.date.LongDateModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.date.ShortDateModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.imprecise.DatelessModel;
import ro.webdata.lido.mapping.processing.timespan.ro.model.imprecise.InaccurateYearModel;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.*;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.date.LongDateRegex;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.date.ShortDateRegex;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.imprecise.DatelessRegex;
import ro.webdata.lido.mapping.processing.timespan.ro.regex.imprecise.InaccurateYearRegex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimespanUtils {
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
            String test = "2 a.chr - 14 p.chr";
            br = new BufferedReader(new StringReader(test));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    String value = StringUtils.stripAccents(readLine);

                    timespanModel = new TimespanModel(value);
                    timespanModel = getMatchedValues(timespanModel, UnknownRegex.UNKNOWN, TimeUtils.UNKNOWN_DATE_PLACEHOLDER);

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

                    timespanModel = getMatchedValues(timespanModel, DatelessRegex.DATELESS, null);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER_INTERVAL, TimeUtils.CHRISTUM_AD_LABEL);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE_INTERVAL, TimeUtils.CHRISTUM_BC_LABEL);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_INTERVAL, TimeUtils.APPROXIMATE_PLACEHOLDER);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.AFTER, TimeUtils.CHRISTUM_AD_LABEL);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.BEFORE, TimeUtils.CHRISTUM_BC_LABEL);
                    timespanModel = getMatchedValues(timespanModel, InaccurateYearRegex.APPROX_AGES_OPTIONS, TimeUtils.APPROXIMATE_PLACEHOLDER);

                    // Firstly, the years consisting of 3 - 4 digits need to be processed
                    timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_SPECIAL_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_3_4_DIGITS_OPTIONS, null);
                    // Secondly, the years consisting of 2 digits need to be processed
                    timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_2_DIGITS_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, YearRegex.YEAR_2_DIGITS_OPTIONS, null);
                    // This call need to be made after all the years processing !!!
                    timespanModel = getMatchedValues(timespanModel, YearRegex.UNKNOWN_YEARS, TimeUtils.UNKNOWN_DATE_PLACEHOLDER);

                    if (timespanModel.getTimespanList().size() > 0)
                        System.out.println(timespanModel.getTimespanList());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: "2 a.chr - 14 p.chr"
    private static TimespanModel getMatchedValues(TimespanModel timespanModel, String regex, String flag) {
        String initialValue = timespanModel.getResidualValue();
        initialValue = TimeSanitizeUtils.sanitizeValue(initialValue, regex)
                .replaceAll(TimespanRegex.AGE_BC, TimeUtils.CHRISTUM_BC_PLACEHOLDER)
                .replaceAll(TimespanRegex.AGE_AD, TimeUtils.CHRISTUM_AD_PLACEHOLDER);

        ArrayList<String> matchedList = timespanModel.getTimespanList();
        String residualValue = initialValue
                .replaceAll(regex, Constants.EMPTY_VALUE_PLACEHOLDER);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initialValue);

        while (matcher.find()) {
            String prepared = prepareValue(matcher.group(), regex, flag);
            System.out.println("matcher.group(): " + matcher.group());
            System.out.println("prepared: " + prepared);
            System.out.println("initialValue: " + initialValue);
            System.out.println("residualValue: " + residualValue);
            System.out.println();
//            matchedList.add(prepared);
            matchedList.add("prepared: \"" + prepared + "\"     initialValue: \"" + initialValue + "\"");
        }

        return new TimespanModel(matchedList, residualValue.trim());
    }

    /**
     * Format date-like, year-like, centuries and millenniums values
     * @param value The original value
     * @param regex A regular expression
     * @param flag An optional value used to differentiate special cases
     * @return The formatted value
     */
    private static String prepareValue(String value, String regex, String flag) {
        if (flag != null && flag.equals(TimeUtils.UNKNOWN_DATE_PLACEHOLDER)) {
            return value
                    .replaceAll(regex, Constants.EMPTY_VALUE_PLACEHOLDER)
                    .trim();
        }

        String prepared = prepareDateTime(value, regex, flag);
        prepared = prepareAges(prepared, regex, flag);
        prepared = prepareTimePeriod(prepared, regex, flag);

        return prepared;
    }

    /**
     * Format the date-like value
     * @param value The original value
     * @param regex A regular expression
     * @param flag An optional value used to differentiate special cases
     * @return The formatted value
     */
    private static String prepareDateTime(String value, String regex, String flag) {
        DateModel dateModel;

        switch (regex) {
            case DateRegex.DATE_DMY_INTERVAL:
            case DateRegex.DATE_DMY_OPTIONS:
                dateModel = new DateModel(value, TimeUtils.DMY_PLACEHOLDER);
                return dateModel.toString();
            case DateRegex.DATE_YMD_INTERVAL:
            case DateRegex.DATE_YMD_OPTIONS:
                dateModel = new DateModel(value, TimeUtils.YMD_PLACEHOLDER);
                return dateModel.toString();
            case ShortDateRegex.DATE_MY_INTERVAL:
            case ShortDateRegex.DATE_MY_OPTIONS:
                ShortDateModel shortDateModel = new ShortDateModel(value, TimeUtils.MY_PLACEHOLDER);
                return shortDateModel.toString();
            case LongDateRegex.LONG_DATE_OPTIONS:
                LongDateModel longDateModel = new LongDateModel(value);
                return longDateModel.toString();
            default:
                return value;
        }
    }

    /**
     * Format the year-like value
     * @param value The original value
     * @param regex A regular expression
     * @param flag An optional value used to differentiate special cases
     * @return The formatted value
     */
    private static String prepareAges(String value, String regex, String flag) {
        switch (regex) {
            case InaccurateYearRegex.AFTER:
            case InaccurateYearRegex.AFTER_INTERVAL:
            case InaccurateYearRegex.APPROX_AGES_INTERVAL:
            case InaccurateYearRegex.APPROX_AGES_OPTIONS:
            case InaccurateYearRegex.BEFORE:
            case InaccurateYearRegex.BEFORE_INTERVAL:
                InaccurateYearModel inaccurateYearModel = new InaccurateYearModel(value, flag);
                return inaccurateYearModel.toString();
            case DatelessRegex.DATELESS:
                DatelessModel datelessModel = new DatelessModel(value);
                return datelessModel.toString();
            case YearRegex.YEAR_2_DIGITS_INTERVAL:
            case YearRegex.YEAR_2_DIGITS_OPTIONS:
            case YearRegex.YEAR_3_4_DIGITS_INTERVAL:
            case YearRegex.YEAR_3_4_DIGITS_SPECIAL_INTERVAL:
            case YearRegex.YEAR_3_4_DIGITS_OPTIONS:
                YearModel yearModel = new YearModel(value);
                return yearModel.toString();
            default:
                return value;
        }
    }

    /**
     * Format centuries and millenniums
     * @param value The original value
     * @param regex A regular expression
     * @param flag An optional value used to differentiate special cases
     * @return The formatted value
     */
    private static String prepareTimePeriod(String value, String regex, String flag) {
        //TODO:
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
}
