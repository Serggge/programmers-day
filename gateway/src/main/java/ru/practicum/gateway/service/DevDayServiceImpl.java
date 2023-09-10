package ru.practicum.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.dto.AnswerDto;
import ru.practicum.gateway.dto.RegisterDto;
import ru.practicum.httpclient.service.HttpClientFactory;
import ru.practicum.httpclient.service.SpringHttpClient;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DevDayServiceImpl implements DevDayService {

    private final SpringHttpClient httpClient;
    private final DtoMapper dtoMapper;
    private String token;

    public DevDayServiceImpl(@Value("${practicum.url}") String serverUrl,
                             @Value("${practicum.token}") String token,
                             DtoMapper dtoMapper) {
        this.httpClient = HttpClientFactory.getClient(serverUrl);
        this.dtoMapper = dtoMapper;
        this.token = token;
    }

    @Override
    @SneakyThrows
    public ResponseEntity<String> sendRequest(RegisterDto dto, HttpServletRequest servletRequest) {
        String jsonString = dtoMapper.mapToJson(dto);
        String path = servletRequest.getServletPath();
        HttpResponse<String> response = httpClient.send(path, servletRequest.getMethod(), jsonString, toHeaders(servletRequest));
        return ResponseEntity.ok(response.body());
    }

    @Override
    @SneakyThrows
    public ResponseEntity<String> sendRequestTask2(HttpServletRequest servletRequest) {
        HttpResponse<String> response = httpClient.send(servletRequest.getServletPath(), servletRequest.getMethod(), null, toHeaders(servletRequest));
        String htmlBody = response.body();
        Pattern pattern = Pattern.compile("<code id=\"message.*/code>");
        Matcher matcher = pattern.matcher(htmlBody);
        String groupg = "";
        if (matcher.find()) {
            groupg = matcher.group(0);
            groupg = groupg.replace("&quot;", "");
            pattern = Pattern.compile("\\{encoded.*\\}");
            matcher = pattern.matcher(groupg);
            if (matcher.find()) {
                groupg = matcher.group(0);
            }
        }
        String encodedLine = groupg.substring(groupg.indexOf(" ") + 1, groupg.indexOf(","));
        int offset = Integer.parseInt(groupg.substring(groupg.lastIndexOf(" ") + 1, groupg.lastIndexOf("}")));
        char[] charArray = encodedLine.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c != ' ') {
                char decodedChar = (c - offset) >= 65 ? (char) (c - offset) : (char) (90 - (c - 65));
                charArray[i] = decodedChar;
            }
        }
        String result = String.valueOf(charArray);
        Map<String, String> header = new HashMap<>();
        header.put("AUTH_TOKEN", token);
        String json = new ObjectMapper().writeValueAsString(new AnswerDto(result));
        httpClient.send(servletRequest.getServletPath(), HttpMethod.POST.name(), json, header);
        return ResponseEntity.ok(result);
    }

    private Map<String, String> toHeaders(HttpServletRequest servletRequest) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (!name.equalsIgnoreCase("content-length") && !name.equalsIgnoreCase("host")) {
                headers.put(name, servletRequest.getHeader(name));
            }
        }
        return headers;
    }
}
