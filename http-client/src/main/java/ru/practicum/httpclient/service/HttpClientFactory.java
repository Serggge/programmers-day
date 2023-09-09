package ru.practicum.httpclient.service;

public final class HttpClientFactory {

    private HttpClientFactory() {
    }

    public static SpringHttpClient getClient(String serverUrl) {
        return new SpringHttpClientImpl(serverUrl);
    }

}
