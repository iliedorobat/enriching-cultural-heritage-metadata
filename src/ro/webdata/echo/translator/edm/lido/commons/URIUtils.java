package ro.webdata.echo.translator.edm.lido.commons;

import ro.webdata.echo.commons.File;

public class URIUtils {
    public static String prepareUri(String domain, String relativeUri) {
        // E.g.:
        // recordId = /CIMEC/35516F8393AD44278314D43908F16FD5
        // NS_REPO_RESOURCE_CHO = http://opendata.cs.pub.ro/resource/cho/
        if (domain.endsWith(File.FILE_SEPARATOR) && relativeUri.startsWith(File.FILE_SEPARATOR)) {
            return domain + relativeUri.substring(1);
        }

        return domain + relativeUri;
    }
}