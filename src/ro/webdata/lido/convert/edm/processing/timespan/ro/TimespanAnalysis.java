package ro.webdata.lido.convert.edm.processing.timespan.ro;

import org.apache.commons.lang3.StringUtils;
import ro.webdata.lido.convert.edm.common.TextUtils;
import ro.webdata.lido.convert.edm.common.constants.FileConstants;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.AgeRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.UnknownRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.DateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.TimePeriodRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.InaccurateRegex;
import ro.webdata.lido.convert.edm.processing.timespan.ro.regex.date.ShortDateRegex;
import ro.webdata.lido.parser.core.ParserDAO;
import ro.webdata.lido.parser.core.ParserDAOImpl;
import ro.webdata.lido.parser.core.leaf.descriptiveMetadata.DescriptiveMetadata;
import ro.webdata.lido.parser.core.leaf.displayDate.DisplayDate;
import ro.webdata.lido.parser.core.leaf.eventDate.EventDate;
import ro.webdata.lido.parser.core.leaf.lido.Lido;
import ro.webdata.lido.parser.core.set.eventSet.EventSet;
import ro.webdata.lido.parser.core.wrap.lidoWrap.LidoWrap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimespanAnalysis {
    private static ParserDAO parserDAO = new ParserDAOImpl();

    /**
     * Extract the timespan from LIDO files<br/>
     * <b>Used in the analysis process</b>
     * @param fileNames The list of LIDO file name
     * @param filePath The full path for the output file
     */
    public static void write(String[] fileNames, String filePath) {
        StringWriter writer = new StringWriter();
        Set<String> list = extractTimespan(fileNames);

        for (String string : list) {
            writer.append(string);
            writer.append("\n");
        }

        TextUtils.write(writer, filePath);
    }

    public static void check(String filePath) {
        //TODO:
        String[] list = {
                UnknownRegex.UNKNOWN,
                DateRegex.DATE_DMY_INTERVAL,
                DateRegex.DATE_YMD_INTERVAL,
                ShortDateRegex.DATE_MY_INTERVAL,
                DateRegex.DATE_DMY_OPTIONS,
                DateRegex.DATE_YMD_OPTIONS,
                ShortDateRegex.DATE_MY_OPTIONS,
                TimePeriodRegex.CENTURY_INTERVAL,
                TimePeriodRegex.CENTURY_OPTIONS,
                TimePeriodRegex.MILLENNIUM_INTERVAL,
                TimePeriodRegex.MILLENNIUM_OPTIONS,
                TimePeriodRegex.OTHER_ROMAN_INTERVAL,
                TimePeriodRegex.OTHER_ROMAN_OPTIONS,
                AgeRegex.AGE_OPTIONS,

                InaccurateRegex.AFTER,
                InaccurateRegex.BEFORE,
                InaccurateRegex.DATELESS,
                InaccurateRegex.APPROX_AGES_INTERVAL,
                InaccurateRegex.APPROX_AGES_OPTIONS
        };

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    boolean check = isMatching(readLine, list);
                    if (!check)
                        System.out.println(readLine);
                }
            }
        } catch (IOException e) {

        }
    }

    /**
     * Extract all timespan to a sorted list
     * @param fileNames The list of paths to LIDO files
     */
    private static Set<String> extractTimespan(String[] fileNames) {
        Set<String> list = new HashSet<>();

        for (int count = 0; count < fileNames.length; count++) {
            String filePath = FileConstants.FILE_PATH + FileConstants.FILE_SEPARATOR
                    + fileNames[count] + FileConstants.FILE_EXTENSION;
            addTimespan(filePath, list);
        }

        Set<String> sorted = new TreeSet<>(list);
        return sorted;
    }

    /**
     * Add timespan to the provided list
     * @param filePath The path to the LIDO file
     * @param list The related list
     */
    private static void addTimespan(String filePath, Set<String> list) {
        LidoWrap lidoWrap = parserDAO.parseLidoFile(filePath);

        for (int i = 0; i < lidoWrap.getLido().size(); i++) {
            Lido lido = lidoWrap.getLido().get(i);
            ArrayList<DescriptiveMetadata> descriptiveMetadataList = lido.getDescriptiveMetadata();

            for (int j = 0; j < descriptiveMetadataList.size(); j++) {
                DescriptiveMetadata descriptiveMetadata = descriptiveMetadataList.get(j);
                ArrayList<EventSet> eventSetList = descriptiveMetadata.getEventWrap().getEventSet();
                addEventDateTimespan(eventSetList, list);
            }
        }
    }

    /**
     * Add event related timespan to the provided list
     * @param eventSetList The list of EventSet items
     * @param list The related list
     */
    private static void addEventDateTimespan(ArrayList<EventSet> eventSetList, Set<String> list) {
        for (int i = 0; i < eventSetList.size(); i++) {
            EventSet eventSet = eventSetList.get(i);
            EventDate eventDate = eventSet.getEvent().getEventDate();

            if (eventDate != null) {
                ArrayList<DisplayDate> displayDateList = eventDate.getDisplayDate();

                for (int k = 0; k < displayDateList.size(); k++) {
                    DisplayDate displayDate = displayDateList.get(k);
                    String timespan = displayDate.getText().toLowerCase();
                    list.add(timespan);
                }
            }
        }
    }

    /**
     * Check if the input value is matching at least one of the regex from the list
     * @param value The input value
     * @param regexList The matching regex list
     * @return
     */
    private static boolean isMatching(String value, String[] regexList) {
        boolean output = false;

        for (int i = 0; i < regexList.length; i++) {
            output = output || isMatching(value, regexList[i]);
        }

        return output;
    }

    /**
     * Check if the input value is matching the regex
     * @param value The input value
     * @param regex The matching regex
     * @return
     */
    private static boolean isMatching(String value, String regex) {
        String matchedValue = StringUtils.stripAccents(value);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(matchedValue);

        while (matcher.find()) {
            return true;
        }

        return false;
    }
}
