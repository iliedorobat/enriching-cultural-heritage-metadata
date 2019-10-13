package ro.webdata.lido.convert.edm.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.YearIntervalModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.TimespanModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.InaccurateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.FullDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.model.date.ShortDateModel;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.AgeRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.InaccurateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.UnknownRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.FullDateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimespanUtils {
    private static final String EMPTY_STRING = "";

    private static final String LEA = "lea";
    private static final String LEA_REPLACEMENT = "##LEA##";
    private static final String MIJ = "mij";
    private static final String MIJ_REPLACEMENT = "##HALF##";

    private static final String URL_SEPARATOR = "/";
    private static final String A_MATCHING_GROUP = "([ ]+a[ \\.]+)";
    private static final String P_MATCHING_GROUP = "([ ]+p[ \\.]+)";
    private static final String CHR_MATCHING_GROUP = "(chr)([ \\.]+|\\z)";

    /**
     * TODO: doc
     * 1. Map every unknown value in order to clear the list by junk elements
     * 2. Map every date-like value
     * 3. Map every age-like value (epoch)
     * @param filePath
     */
    public static void read(String filePath) {
        try {
            TimespanModel timespanModel;
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // used for testing
            String test = "s:17;a:1622;l:12;z:30";
            test = "20 i.e.n";
//            br = new BufferedReader(new StringReader(test));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    timespanModel = new TimespanModel(StringUtils.stripAccents(readLine));

                    timespanModel = getMatchedValues(timespanModel, UnknownRegex.UNKNOWN, null);
                    timespanModel = getMatchedValues(timespanModel, FullDateRegex.DATE_DMY_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, FullDateRegex.DATE_YMD_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_INTERVAL, null);
                    timespanModel = getMatchedValues(timespanModel, FullDateRegex.DATE_DMY_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, FullDateRegex.DATE_YMD_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, ShortDateRegex.DATE_MY_OPTIONS, null);
                    timespanModel = getMatchedValues(timespanModel, AgeRegex.AGE_OPTIONS, null);
//                    timespanModel.clearTimespanList();

//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.AFTER, InaccurateModel.AFTER);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.BEFORE, InaccurateModel.BEFORE);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.DATELESS, InaccurateModel.UNDATED);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.APPROX_AGES_INTERVAL, InaccurateModel.APPROXIMATE);
//                    timespanModel = getMatchedValues(timespanModel, InaccurateRegex.APPROX_AGES_OPTIONS, InaccurateModel.APPROXIMATE);

//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_INTERVAL, null);
//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.CENTURY_OPTIONS, null);
//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_INTERVAL, null);
//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.MILLENNIUM_OPTIONS, null);
//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_ROMAN_INTERVAL, null);
//                    timespanModel = getMatchedValues(timespanModel, TimePeriodRegex.OTHER_ROMAN_OPTIONS, null);
//
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
        String residualValue = initialValue.replaceAll(regex, EMPTY_STRING);

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
        FullDateModel fullDateModel;
        ShortDateModel shortDateModel;

        switch (regex) {
            case FullDateRegex.DATE_DMY_INTERVAL:
            case FullDateRegex.DATE_DMY_OPTIONS:
                fullDateModel = new FullDateModel(value, FullDateModel.DMY);
                return fullDateModel.toString();
            case FullDateRegex.DATE_YMD_INTERVAL:
            case FullDateRegex.DATE_YMD_OPTIONS:
                fullDateModel = new FullDateModel(value, FullDateModel.YMD);
                return fullDateModel.toString();
            case ShortDateRegex.DATE_MY_INTERVAL:
            case ShortDateRegex.DATE_MY_OPTIONS:
                shortDateModel = new ShortDateModel(value, ShortDateModel.MY);
                return shortDateModel.toString();
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
//            case TimespanRegex.OTHER_ROMAN_INTERVAL:
//            case TimespanRegex.OTHER_ROMAN_OPTIONS:
//                return value.replaceAll(MIJ_REPLACEMENT, MIJ);
            case UnknownRegex.UNKNOWN:
            default:
                return value;
        }
    }





}
