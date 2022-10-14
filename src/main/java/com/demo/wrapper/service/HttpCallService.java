package com.demo.wrapper.service;

import com.demo.wrapper.model.edc.catalog.Catalog;
import com.demo.wrapper.model.edc.policy.AtomicConstraint;
import com.demo.wrapper.model.edc.policy.LiteralExpression;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static java.lang.String.format;

@Component
public class HttpCallService {

    private static final Logger logger = LoggerFactory.getLogger(HttpCallService.class);

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpCallService(OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        objectMapper.registerSubtypes(AtomicConstraint.class, LiteralExpression.class);
    }


    public Catalog getCatalogFromProvider(String consumerUrl, String providerIdsUrl, Map<String, String> headers) throws IOException {
        var url = consumerUrl + providerIdsUrl;
        var request = new Request.Builder().url(url);
        headers.forEach(request::addHeader);
        return (Catalog) sendRequest(request.build(), Catalog.class);
    }


    public Object sendRequest(Request request, Class<?> responseObject) throws IOException {
        logger.info("HttpCallService :: sendRequest --> url : {}", request.url().url());
        try (var response = httpClient.newCall(request).execute()) {
            var body = response.body();
            if (!response.isSuccessful() || body == null) {
                throw new RuntimeException(format("Control plane responded with: %s %s", response.code(), body != null ? body.string() : ""));
            }
            String res = body.string();
            return objectMapper.readValue(res, responseObject);
        } catch (Exception e) {
            String key = "sendRequest_" + request.url().url();
            CacheService.socketException.put(key, CacheService.socketException.containsKey(key) ? CacheService.socketException.get(key) + 1 : 1);
            throw e;
        }
    }
}
