package ru.practicum.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.httpclient.service.StandALone;

@SpringBootApplication
public class HttpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpClientApplication.class, args);
    }

}
