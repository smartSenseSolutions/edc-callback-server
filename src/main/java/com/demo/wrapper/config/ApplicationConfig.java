package com.demo.wrapper.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationConfig {
    private final Long connectionTimeout;
    private final Long writeTimeout;
    private final Long readTimeout;

    @Autowired
    public ApplicationConfig(@Value("${http.connectionTimeout}") Long connectionTimeout,
                             @Value("${http.writeTimeout}") Long writeTimeout,
                             @Value("${http.readTimeout}") Long readTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }
}
