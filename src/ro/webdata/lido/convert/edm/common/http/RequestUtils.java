package ro.webdata.lido.convert.edm.common.http;

import ro.webdata.lido.convert.edm.common.ResourceUtils;

import java.net.HttpURLConnection;

public class RequestUtils {
    private RequestUtils() {};

    /**
     * Get the status code of the HTTP request and close the connection
     * @param address The URI
     * @return The status code
     */
    public static Integer httpGetStatusCode(String address) {
        HttpURLConnection connection = Request.sendGet(address);
        connection.disconnect();
        return Request.statusCode(connection);
    }

    /**
     * Get the content of the HTTP request anc close the connection
     * @param address The URI
     * @return The content
     */
    public static StringBuilder httpGetContent(String address) {
        HttpURLConnection connection = Request.sendGet(address);
        connection.disconnect();
        return Request.content(connection);
    }

    /**
     * Check if DBPedia has defined the passed object name
     * @param objectName The name of an element (could be a country, a name, a concept etc.)
     * @return True if DBPedia has defined the passed object name, otherwise return false
     */
    public static boolean isValidDBPedia(String objectName) {
        Integer statusCode = httpGetStatusCode(
                ResourceUtils.generateDBPediaURI(objectName)
        );
        int prefix = statusCode / 100;

        //TODO:
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return prefix == 2 || prefix == 3;
    }
}