package ro.webdata.echo.translator.commons;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

// https://mkyong.com/java/java-11-httpclient-examples/
public class SyncHttpClient {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> callApi(String link) {
        HttpRequest request = HttpRequest.newBuilder()
                .timeout(Duration.ofSeconds(300))
                .GET()
                .uri(URI.create(link))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("The current request is canceled, but the process continues...");
            return null;
        }
    }
}
