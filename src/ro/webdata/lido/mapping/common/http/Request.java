package ro.webdata.lido.mapping.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class Request {
    private static final String HTTP_GET = "GET";
    private static final int TIMEOUT = 5000;

    private Request() {};

    /**
     * Establish a GET HTTP request
     * @param address The URI
     * @return The connection object
     */
    public static HttpURLConnection sendGet(String address) {
        HttpURLConnection connection = null;

        try {
            connection = getConnection(address);
            connection.setRequestMethod(HTTP_GET);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * Get the status code of the HTTP request
     * @param connection The connection object
     * @address The http address
     * @return The status code
     */
    public static int statusCode(HttpURLConnection connection, String address) {
        int code = 408;

        try {
            code = connection.getResponseCode();
        } catch (IOException e) {
            System.err.println("Read timed out: " + address);
        }

        return code;
    }

    /**
     * Get the content of the HTTP request
     * @param connection The connection object
     * @return The requested content
     */
    public static StringBuilder content(HttpURLConnection connection) {
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuilder response = null;

        try {
            is = new InputStreamReader(connection.getInputStream());
            br = new BufferedReader(is);
            response = new StringBuilder();
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Establish the HTTP connection
     * @param address The URI
     * @return The connection object
     */
    private static HttpURLConnection getConnection(String address) {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
