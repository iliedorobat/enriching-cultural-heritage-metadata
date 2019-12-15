package ro.webdata.translator.edm.approach.event.lido.common.http;

import ro.webdata.translator.edm.approach.event.lido.common.ResourceUtils;

import java.net.HttpURLConnection;

public class RequestUtils {
    private RequestUtils() {};

    /**
     * Get the status code of the HTTP request and close the connection
     * @param address The URI
     * @return The status code
     */
    public static int httpGetStatusCode(String address) {
        HttpURLConnection connection = Request.sendGet(address);
        connection.disconnect();
        return Request.statusCode(connection, address);
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
        int statusCode = httpGetStatusCode(
                ResourceUtils.generateDBPediaURI(objectName)
        );
        int prefix = statusCode / 100;

        //TODO:
//        try {
//            TimeUnit.SECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return prefix == 2 || prefix == 3;
    }
}
