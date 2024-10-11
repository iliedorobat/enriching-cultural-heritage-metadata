package ro.webdata.echo.fetcher.museums.core.inp;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.accessor.MuseumAccessors;
import ro.webdata.echo.fetcher.museums.commons.CollectionsUtils;
import ro.webdata.echo.fetcher.museums.commons.FilePath;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InpFetcher {
    public static ArrayList<TreeMap<String, Object>> getPairs(String lang) {
        ArrayList<TreeMap<String, Object>> list = new ArrayList<>();
        String path = FilePath.getInpGuidePath();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String readLine;
            int lineIndex = 0;

            System.out.println("Starting to fetch INP data (lang = " + lang + ")...");

            while ((readLine = br.readLine()) != null) {
                System.out.println("Getting record no. " + lineIndex + "...");

                // The first line contains only the keys
                if (readLine.trim().length() > 0 && lineIndex > 0) {
                    TreeMap<String, Object> pairs = getInpEntry(readLine, lang);
                    if (!pairs.isEmpty()) {
                        list.add(pairs);
                    }
                }

                lineIndex++;
            }

            System.out.println("INP data fetching is finished!");
        } catch (FileNotFoundException e) {
            System.err.println("The file " + path + " has not been found.");
        } catch (IOException e) {
            System.err.println("Error at reading the file " + path + " from the disk.");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("The file 'Buffered Reader' could not be closed.");
            }
        }

        return list;
    }

    public static TreeMap<String, Object> getInpEntry(String inputLine, String lang) {
        String[] array = inputLine.split(FilePath.SEPARATOR_PIPE);
        TreeMap<String, Object> entryMap = new TreeMap<>();

        addInpEntry(entryMap, MuseumAccessors.MUSEUM_ACCREDITATION, array, 37, true);
        addInpEntry(entryMap, MuseumAccessors.MUSEUM_CODE, array, 0);
        addInpEntry(entryMap, MuseumAccessors.MUSEUM_FOUNDED, array, 17);
//        addInpEntry(entryMap, MuseumAccessors.INSTITUTION_CODE, array, 30);

        addInpEntry(entryMap, MuseumAccessors.BUILDING_LMI_CODE, array, 33);

        addInpEntry(entryMap, MuseumAccessors.CONTACT_DIRECTOR, array, 29);
        addInpEntry(entryMap, MuseumAccessors.CONTACT_EMAIL, array, 28, true);
        addInpEntry(entryMap, MuseumAccessors.CONTACT_FAX, array, 13, true);
        addInpEntry(entryMap, MuseumAccessors.CONTACT_PHONE, array, 11, true);
        addInpEntry(entryMap, MuseumAccessors.CONTACT_WEB, array, 27, true);

        addInpEntry(entryMap, MuseumAccessors.LOCATION_ADDRESS, array, 9);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_ADM_UNIT, array, 8);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_COUNTY, array, 2);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_GEO_LATITUDE, array, 40);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_GEO_LONGITUDE, array, 41);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_GEO_TARGET, array, 42);

        addInpEntry(entryMap, MuseumAccessors.LOCATION_LOCALITY_CODE, array, 34);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_LOCALITY_NAME, array, 7);
        addInpEntry(entryMap, MuseumAccessors.LOCATION_ZIP_CODE, array, 10);

        switch (lang) {
            case Const.LANG_EN:
                addInpEntry(entryMap, MuseumAccessors.MUSEUM_NAME, array, 5);

                addInpEntry(entryMap, MuseumAccessors.BUILDING_DESCRIPTION, array, 22);
                addInpEntry(entryMap, MuseumAccessors.COLLECTION_IMPORTANCE, array, 39);
                addInpEntry(entryMap, MuseumAccessors.COLLECTION_MAIN_PROFILE, array, 36);
                addInpEntry(entryMap, MuseumAccessors.CONTACT_TIME_TABLE, array, 16, true);
                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_DETAILS, array, 31);
                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_SUMMARY, array, 20);
                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_HISTORIC, array, 24);
                addInpEntry(entryMap, MuseumAccessors.LOCATION_ACCESS, array, 26);

                break;
            case Const.LANG_RO:
                addInpEntry(entryMap, MuseumAccessors.MUSEUM_NAME, array, 3);

                addInpEntry(entryMap, MuseumAccessors.BUILDING_DESCRIPTION, array, 21);
                addInpEntry(entryMap, MuseumAccessors.COLLECTION_IMPORTANCE, array, 38);
                addInpEntry(entryMap, MuseumAccessors.COLLECTION_MAIN_PROFILE, array, 32);
                addInpEntry(entryMap, MuseumAccessors.CONTACT_TIME_TABLE, array, 15, true);

                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_DETAILS, array, 18);
                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_SUMMARY, array, 19);
                addInpEntry(entryMap, MuseumAccessors.DESCRIPTION_HISTORIC, array, 23);
                addInpEntry(entryMap, MuseumAccessors.LOCATION_ACCESS, array, 25);

                break;
            default:
                System.err.println("The \"" + lang + "\" language is not supported.");
                break;
        }

        return entryMap;
    }

    private static void addInpEntry(Map<String, Object> entryMap, String key, String[] values, int index) {
        ArrayList<String> keys = new ArrayList<>(getKeys());
        if (values.length > index) {
            String value = values[index];
            if (value != null && !value.trim().isEmpty()) {
                entryMap.put(keys.get(index), value.trim());
            }
        }
    }

    private static void addInpEntry(Map<String, Object> entryMap, String key, String[] values, int index, Boolean strToArr) {
        ArrayList<String> keys = new ArrayList<>(getKeys());
        if (values.length > index) {
            String value = values[index];
            List<String> array = CollectionsUtils.splitString(value, ";");

            if (array.size() > 0) {
                entryMap.put(keys.get(index), array);
            }
        }
    }

    public static LinkedHashSet<String> getKeys() {
        LinkedHashSet<String> keys = new LinkedHashSet<>();
        String path = FilePath.getInpGuidePath();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String readLine;
            int lineIndex = 0;

            while ((readLine = br.readLine()) != null) {
                // The first line contains only the keys
                if (readLine.trim().length() > 0 && lineIndex == 0) {
                    String[] array = readLine.split(FilePath.SEPARATOR_PIPE);
                    keys.addAll(Arrays.asList(array));
                }
                lineIndex++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file " + path + " has not been found.");
        } catch (IOException e) {
            System.err.println("Error at reading the file " + path + " from the disk.");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("The file 'Buffered Reader' could not be closed.");
            }
        }

        return keys;
    }
}
