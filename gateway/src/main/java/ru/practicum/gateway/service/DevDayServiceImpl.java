package ru.practicum.gateway.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.dto.RegisterDto;
import ru.practicum.httpclient.service.HttpClientFactory;
import ru.practicum.httpclient.service.SpringHttpClient;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Service
public class DevDayServiceImpl implements DevDayService {

    private final SpringHttpClient httpClient;
    private final DtoMapper dtoMapper;

    public DevDayServiceImpl(@Value("${practicum.url}") String serverUrl, DtoMapper dtoMapper) {
        this.httpClient = HttpClientFactory.getClient(serverUrl);
        this.dtoMapper = dtoMapper;
    }

    @Override
    @SneakyThrows
    public ResponseEntity<String> sendRequest(RegisterDto dto, HttpServletRequest servletRequest) {
        String jsonString = dtoMapper.mapToJson(dto);
        String path = servletRequest.getServletPath();
        HttpResponse<String> response = httpClient.send(path, servletRequest.getMethod(), jsonString, toHeaders(servletRequest));
        return ResponseEntity.ok(response.body());
    }

    private Map<String, String> toHeaders(HttpServletRequest servletRequest) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, servletRequest.getHeader(name));
        }
        return headers;
    }
}
