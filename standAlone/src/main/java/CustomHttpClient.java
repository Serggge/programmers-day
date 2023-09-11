import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    //зашифрованное число
    private static final long pass = 3078115218L;

    private final HttpClient httpClient;

    public CustomHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void start() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String path = "/dev-day/task/3";
        String method = "POST";
        String hex = "";
        boolean notFound = true;
        //начало локальной проверки
        long max = 3722304989L;
        long min = 0;
        while (notFound) {
            hex = toHex((max + min) / 2);
            String result = checkPass(hex);
            if (result.equals("equal")) {
                notFound = false;
            } else if (result.equals("<pass")) {
                min = (max + min) / 2 + 1;
            } else {
                max = (max + min) / 2 - 1;
            }
        }
        System.out.println(hex);
//        while (notFound) {
//            hex = toHex((max - min) / 2);
//            HttpResponse<String> response = send(path, method, objectMapper.writeValueAsString(new Answer(hex)), toHeaders());
//            if (response.statusCode() == 200) {
//                notFound = false;
//            } else if (hex.contains("<pass")) {
//                min = (max - min) / 2 + 1;
//            } else {
//                max = (max - min) / 2 - 1;
//            }
//        }
    }

    private String toHex(long number) {
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            long ost = number % 16;
            if (ost == 10) {
                sb.append("A");
            } else if (ost == 11) {
                sb.append("B");
            } else if (ost == 12) {
                sb.append("C");
            } else if (ost == 13) {
                sb.append("D");
            } else if (ost == 14) {
                sb.append("E");
            } else if (ost == 15) {
                sb.append("F");
            } else {
                sb.append(ost);
            }
            number /= 16;
        }
        return sb.reverse().toString();
    }

    private static String checkPass(String checkedNumber) {
        int degree = checkedNumber.length() - 1;
        long result = 0L;
        for (int i = 0; i < checkedNumber.length(); i++) {
            char c = checkedNumber.charAt(i);
            int number = -1;
            switch (c) {
                case 'A':
                    number = 10;
                    break;
                case 'B':
                    number = 11;
                    break;
                case 'C':
                    number = 12;
                    break;
                case 'D':
                    number = 13;
                    break;
                case 'E':
                    number = 14;
                    break;
                case 'F':
                    number = 15;
                    break;
                default:
                    number = Integer.parseInt(String.valueOf(c));
            }
            result += number * Math.pow(16, degree--);
        }
        if (result > pass) {
            return ">pass";
        } else if (result < pass) {
            return "<pass";
        } else {
            return "equal";
        }
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
