package ru.practicum.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.gateway.dto.AnswerDto;
import ru.practicum.gateway.dto.RegisterDto;
import ru.practicum.httpclient.service.HttpClientFactory;
import ru.practicum.httpclient.service.SpringHttpClient;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
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
        //HttpResponse<String> response = httpClient.send(servletRequest.getServletPath(), servletRequest.getMethod(), null, toHeaders(servletRequest));
        String html = "";
        try (BufferedReader buff = new BufferedReader(new FileReader("/home/serggge/Documents/task2.html"))) {
            StringBuilder sb = new StringBuilder();
            while (buff.ready()) {
                sb.append(buff.readLine());
            }
            html = sb.toString();
        }

        //String html = response.body();

        //System.out.println(html);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("code");
        System.out.println(elements.attr("code"));
        return ResponseEntity.ok("");
//        String encodedLine = groupg.substring(groupg.indexOf(" ") + 1, groupg.indexOf(","));
//        int offset = Integer.parseInt(groupg.substring(groupg.lastIndexOf(" ") + 1, groupg.lastIndexOf("}")));
//        char[] charArray = encodedLine.toCharArray();
//        for (int i = 0; i < charArray.length; i++) {
//            char c = charArray[i];
//            if (c != ' ') {
//                char decodedChar = (c - offset) >= 65 ? (char) (c - offset) : (char) (90 - (c - 65));
//                charArray[i] = decodedChar;
//            }
//        }
//        String result = String.valueOf(charArray);
//        Map<String, String> header = new HashMap<>();
//        header.put("AUTH_TOKEN", token);
//        String json = new ObjectMapper().writeValueAsString(new AnswerDto(result));
//        httpClient.send(servletRequest.getServletPath(), HttpMethod.POST.name(), json, header);
//        return ResponseEntity.ok(result);
    }

    private Map<String, String> toHeaders(HttpServletRequest servletRequest) {
        Enumeration<String> headersNames = servletRequest.getHeaderNames();
        while ((headersNames.hasMoreElements())) {
            System.out.println(headersNames.nextElement());
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("AUTH_TOKEN", token);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "*/*");
        headers.put("user-agent", servletRequest.getHeader("user-agent"));
        return headers;
    }
}
