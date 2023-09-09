package ru.practicum.httpclient.service;

import lombok.SneakyThrows;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpringHttpClientImpl implements SpringHttpClient {

    private final HttpClient httpClient;
    private final String serverUrl;

    public SpringHttpClientImpl(String serverUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl= serverUrl;
    }

    @Override
    @SneakyThrows
    public HttpResponse<String> send(String path, String method, String jsonBody, Map<String, String> headers) {
        HttpRequest.BodyPublisher body = (jsonBody == null || jsonBody.isBlank())
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8);
        List<String> hdrsList = new ArrayList<>();
        headers.forEach((key, value) -> {
            if (!"content-length".equalsIgnoreCase(key) && !"host".equalsIgnoreCase(key)) {
                hdrsList.add(key);
                hdrsList.add(value);
            }
        });
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/" + path))
                .method(method, body)
                .headers(hdrsList.toArray(new String[0]))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
