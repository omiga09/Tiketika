package com.tiketika.Tiketika.common.auth.services.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class SmsService {

    private final RestClient restClient;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String LOWER_ALPHANUM = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${app.sms.clientId}")
    private String clientId;

    @Value("${app.sms.clientSecret}")
    private String clientSecret;

    @Value("${app.sms.source}")
    private String source;

    @Value("${app.sms.enabled:true}")
    private boolean smsEnabled;

    public SmsService(@Qualifier("smsRestClient") RestClient restClient) {

        this.restClient = restClient;
    }

    public String sendSms(String phone, String message) {
        if (!smsEnabled) {
            String info = String.format(
                    "SMS disabled. Would send to %s: %s",
                    phone, message
            );
            log.warn(info);
            return info;
        }

        String normalizedPhone = normalizePhone(phone);
        String reference =  System.currentTimeMillis() + generateRandom(LOWER_ALPHANUM,12);

        var payload = Map.of(
                "auth", Map.of(
                        "clientId", clientId,
                        "clientSecret", clientSecret
                ),
                "messages", List.of(
                        Map.of(
                                "text", message,
                                "msisdn", normalizedPhone,
                                "source", source,
                                "reference", reference
                        )
                )
        );

        log.debug("SMS payload for {} (ref={}): {}", normalizedPhone, reference, payload);

        try {
            String body = restClient.post()
                    .uri("/api/sms/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        String errorBody = safeBody(response);
                        log.error("SMS API 4xx for {} (ref={}): status={}, body={}",
                                normalizedPhone, reference, response.getStatusCode(), errorBody);
                        throw new SmsGatewayException(
                                "Client error from SMS API: " + response.getStatusCode(),
                                response.getStatusCode().value(),
                                errorBody
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        String errorBody = safeBody(response);
                        log.error("SMS API 5xx for {} (ref={}): status={}, body={}",
                                normalizedPhone, reference, response.getStatusCode(), errorBody);
                        throw new SmsGatewayException(
                                "Server error from SMS API: " + response.getStatusCode(),
                                response.getStatusCode().value(),
                                errorBody
                        );
                    })
                    .body(String.class);

            log.info("SMS API success for {} (ref={}): {}", normalizedPhone, reference, body);
            return body;

        } catch (RestClientException ex) {
            log.error("SMS API call failed for {} (ref={}): {}",
                    normalizedPhone, reference, ex.getMessage(), ex);
            throw ex;
        }
    }

    private static String generateRandom(String charset, int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(charset.charAt(RANDOM.nextInt(charset.length())));
        }
        return sb.toString();
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) return phone;
        phone = phone.trim();
        if (phone.startsWith("0")) return "255" + phone.substring(1);
        if (phone.startsWith("+")) return phone.substring(1);
        return phone;
    }

    private String safeBody(ClientHttpResponse response) {
        try {
            String body = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            return body != null ? body : "";
        } catch (IOException e) {
            log.warn("Failed to read SMS error body: {}", e.getMessage());
            return "";
        }
    }

    public static class SmsGatewayException extends RuntimeException {
        private final int statusCode;
        private final String responseBody;

        public SmsGatewayException(String message, int statusCode, String responseBody) {
            super(message);
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }

        public int getStatusCode() {

            return statusCode;
        }

        public String getResponseBody() {

            return responseBody;
        }
    }

}
