package ro.webdata.echo.fetcher.museums;

import ro.webdata.echo.commons.Const;
import ro.webdata.echo.commons.accessor.MuseumAccessors;
import ro.webdata.echo.fetcher.museums.commons.DataFormatter;
import ro.webdata.echo.fetcher.museums.commons.DatasetUtils;
import ro.webdata.echo.fetcher.museums.commons.FilePath;
import ro.webdata.echo.fetcher.museums.core.DataConsolidation;
import ro.webdata.echo.fetcher.museums.core.DataCuration;
import ro.webdata.echo.fetcher.museums.core.DataFetch;
import ro.webdata.echo.fetcher.museums.core.DataPreparation;

public class Main {
    public static void main(String[] args) {
        System.out.println("START\n");

        // 1. Get the raw data from the data.gov.ro portal
        // Download manually the "inp-ghidul-muzelor-2017-05-18.csv" file
        // from https://data.gov.ro/dataset/ghidul-muzeelor-din-romania

        // 2. Get the CIMEC and INP raw data
        DataFetch.writeCimecJson(Const.LANG_EN);
        DataFetch.writeCimecJson(Const.LANG_RO);
        DataFetch.writeInpJson(Const.LANG_EN);
        DataFetch.writeInpJson(Const.LANG_RO);

        // 3. Prepare the data
        DataCuration.writeCimecJson(Const.LANG_EN);
        DataCuration.writeCimecJson(Const.LANG_RO);
        DataCuration.writeInpJson(Const.LANG_EN);
        DataCuration.writeInpJson(Const.LANG_RO);

        // 4. Merge the CIMEC and INP dataset
        DataConsolidation.writeFinalJson(Const.LANG_EN);
        DataConsolidation.writeFinalJson(Const.LANG_RO);

        DataPreparation.prepareGeoLocation(Const.LANG_EN);
        DataPreparation.prepareGeoLocation(Const.LANG_RO);

        System.out.println("\nEND");
    }

    // Print the CIMEC and INP keys (used for DataMapping.getKeyName mapping method)
    private static void printKeys() {
        DatasetUtils.printKeys(FilePath.getCimecRawJsonPath(Const.LANG_EN), Const.LANG_EN);
        DatasetUtils.printKeys(FilePath.getCimecRawJsonPath(Const.LANG_RO), Const.LANG_RO);
        DatasetUtils.printKeys(FilePath.getInpRawJsonPath(Const.LANG_EN), Const.LANG_EN);
        DatasetUtils.printKeys(FilePath.getInpRawJsonPath(Const.LANG_RO), Const.LANG_RO);
    }

    private static void test() {
        String phone = "; 0266.335.705";
        phone = "0244.367.461   si   2013344778";
        System.out.println(
                DataFormatter.format(MuseumAccessors.CONTACT_PHONE, phone)
        );
    }
}
