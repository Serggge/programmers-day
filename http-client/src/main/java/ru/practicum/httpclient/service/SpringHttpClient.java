package ru.practicum.httpclient.service;

import java.net.http.HttpResponse;
import java.util.Map;

public interface SpringHttpClient {

    HttpResponse<String> send(String path, String method, String jsonBody, Map<String, String> headers);

}
