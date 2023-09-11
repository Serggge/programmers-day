import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomHttpClient {

    private final String serverUrl = "http://ya.praktikum.fvds.ru:8080";
    private final String token = "08589358-cae6-4612-9167-131e5ae27908";

    private final HttpClient httpClient;

    public CustomHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void start() throws Exception {
        String path = "/dev-day/task/2";
        String method = "GET";
        HttpResponse<String> response = send(path, method, null, toHeaders());
        System.out.println(response.body());

    }

    public HttpResponse<String> send(String path, String method, String jsonBody, Map<String, String> headers) throws Exception {
        HttpRequest.BodyPublisher body = (jsonBody == null || jsonBody.isBlank())
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8);
        List<String> hdrsList = new ArrayList<>();
        headers.forEach((key, value) -> {
            hdrsList.add(key);
            hdrsList.add(value);
        });
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, body)
                .headers(hdrsList.toArray(new String[0]))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private Map<String, String> toHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_TOKEN", token);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");
        return headers;
    }
}
