package ro.webdata.lido.convert.edm.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.lido.convert.edm.common.constants.Constants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.YearIntervalModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.TimespanModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.InaccurateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.DateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.LongDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.ShortDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.*;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.LongDateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            String test = "s:17;a:1622;l:12;z:30";
            test = "17 nov. 375-9 aug. 378 a.chr.";
//            br = new BufferedReader(new StringReader(test));
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

                    timespanModel.clearTimespanList();

//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.AFTER, InaccurateModel.AFTER);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.BEFORE, InaccurateModel.BEFORE);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.DATELESS, InaccurateModel.UNDATED);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.APPROX_AGES_INTERVAL, InaccurateModel.APPROXIMATE);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.APPROX_AGES_OPTIONS, InaccurateModel.APPROXIMATE);

                    if (timespanModel.getTimespanList().size() > 0)
                        System.out.println(timespanModel.getTimespanList());
                }
            }
        } catch (IOException e) {

        }
    }

    private static TimespanModel getMatchedValues(TimespanModel timespanModel, String regex, String flag) {
        String initialValue = timespanModel.getResidualValue();
        ArrayList<String> matchedList = timespanModel.getTimespanList();
        String residualValue = initialValue
                .replaceAll(TimespanRegex.AGE_BC, TimespanRegex.CHRISTUM_BC)
                .replaceAll(TimespanRegex.AGE_AD, TimespanRegex.CHRISTUM_AD)
                .replaceAll(regex, Constants.EMPTY_VALUE_PLACEHOLDER);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initialValue);

        while (matcher.find()) {
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
        InaccurateModel inaccurateModel;

        switch (regex) {
//            case TimespanRegex.AGES_4_INTERVAL:
//            case TimespanRegex.AGES_1_3_INTERVAL:
//            case TimespanRegex.AGES_APPROX_INTERVAL:
//                dateIntervalModel = new DateIntervalModel(value);
//                return dateIntervalModel.toString();
//            case TimespanRegex.AGES_APPROX:
//            case TimespanRegex.AGES_1_4_UNCERTAIN:
//            case TimespanRegex.AGES_1_4_CHRISTUM_GROUP:
            case InaccurateRegex.AFTER:
            case InaccurateRegex.BEFORE:
            case InaccurateRegex.DATELESS:
            case InaccurateRegex.APPROX_AGES_INTERVAL:
            case InaccurateRegex.APPROX_AGES_OPTIONS:
                inaccurateModel = new InaccurateModel(value, flag);
                return inaccurateModel.toString();
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





}
