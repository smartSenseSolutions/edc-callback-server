package com.demo.wrapper.web;

import com.demo.wrapper.model.EndpointDataReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EdcCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(EdcCallbackController.class);

    private final OkHttpClient client;

    private final ObjectMapper objectMapper;
    private final Map<String, Object> callBackMap = new HashMap<>();


    @PostMapping(value = "/standalone-callback")
    public void edcCallbackReceiver(@RequestBody EndpointDataReference dataReference) {
        var transferId = dataReference.getId();

        Request request = new Request.Builder()
                .url(dataReference.getEndpoint())
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode())
                .get()
                .build();

        if(callBackMap.containsKey(transferId)){
            return;
        }

        try (var response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                logger.info("Transfer Process Has been completed");
                callBackMap.put(transferId, objectMapper.readValue(response.body().string(), Map.class));
            } else {
                logger.error("Transfer process failed with status {} and response body {}", response.code(), response.body().string());
            }
        } catch (Exception e) {
            logger.error("Error while fetching data for transfer id: {}", transferId, e);
        }
    }

    @GetMapping(value = "/standalone-callback/{transferProcessId}")
    public ResponseEntity<Map<String, Object>> getEdcCallback(@PathVariable("transferProcessId") String transferProcessId) {
        if (!callBackMap.containsKey(transferProcessId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(transferProcessId, callBackMap.getOrDefault(transferProcessId, null)));
    }

    @DeleteMapping(value = "/standalone-callback/{transferProcessId}")
    public ResponseEntity<Object> removeEdcCallback(@PathVariable("transferProcessId") String transferProcessId) {
        if (!callBackMap.containsKey(transferProcessId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        callBackMap.remove(transferProcessId);
        return ResponseEntity.noContent().build();
    }
}
