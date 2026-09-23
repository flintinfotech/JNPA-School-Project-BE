package com.flint.sample_be_springboot.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Map;

@Slf4j
@Service
public class NotificationService {

    @Value("${msg91.authKey}")
    private String authKey;

    @Value("${msg91.templateId}")
    private String templateId;

    private final WebClient webClient;

    public NotificationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String sendNotification(String mobileNumber, String attendanceStatus) {
        log.info("Notification request for mobile: {}", mobileNumber);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("control.msg91.com")
                        .path("/api/v5/notification")
                        .queryParam("template_id", templateId)
                        .queryParam("mobile", mobileNumber)
                        .queryParam("attendanceStatus", attendanceStatus)
                        .queryParam("authkey", authKey)
                        .build()
                )
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "Param1", "value1",
                        "Param2", "value2",
                        "Param3", "value3",
                        "Param4", "value4"
                ))
                .retrieve() // thread waits until the response is received
                .bodyToMono(String.class)// returns only the body
//                .toEntity(String.class)// return the entire http response
                .block();
    }

}
