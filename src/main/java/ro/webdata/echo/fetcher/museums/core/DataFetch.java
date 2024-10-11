package ro.webdata.echo.fetcher.museums.core;

import com.google.gson.Gson;
import ro.webdata.echo.commons.File;
import ro.webdata.echo.fetcher.museums.commons.FilePath;
import ro.webdata.echo.fetcher.museums.core.cimec.CimecFetcher;
import ro.webdata.echo.fetcher.museums.core.inp.InpFetcher;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Download the raw data:
 * <ul>
 *     <li>CIMEC dataset: http://ghidulmuzeelor.cimec.ro/</li>
 *     <li>TODO: INP dataset: https://data.gov.ro/dataset/ghidul-muzeelor-din-romania</li>
 * </ul>
 */
public class DataFetch {
    private static final Gson gson = new Gson();

    public static void writeCimecJson(String lang) {
        String path = FilePath.getCimecRawJsonPath(lang);
        ArrayList<TreeMap<String, Object>> pairs = CimecFetcher.getPairs(lang);
        String json = gson.toJson(pairs);
        File.write(json, path, false);
    }

    public static void writeInpJson(String lang) {
        String path = FilePath.getInpRawJsonPath(lang);
        ArrayList<TreeMap<String, Object>> pairs = InpFetcher.getPairs(lang);
        String json = gson.toJson(pairs);
        File.write(json, path, false);
    }
}
