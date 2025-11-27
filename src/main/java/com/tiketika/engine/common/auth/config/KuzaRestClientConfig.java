package com.tiketika.engine.common.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class KuzaRestClientConfig {
    @Value("${app.sms.url}")
    private String smsUrl;

    @Bean(name = "smsRestClient")
    RestClient smsRestClient() {
        var rf = new SimpleClientHttpRequestFactory();
        return RestClient.builder()
                .baseUrl(smsUrl)
                .requestFactory(rf)
                .build();
    }

}
