package com.demo.wrapper.web;

import com.demo.wrapper.model.CommonResponse;
import com.demo.wrapper.model.EndpointDataReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class EdcCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(EdcCallbackController.class);

    private final Long connectionTimeout;
    private final Long writeTimeout;
    private final Long readTimeout;

    private final ObjectMapper objectMapper;

    public EdcCallbackController(@Value("${http.connectionTimeout}") Long connectionTimeout,
                                 @Value("${http.writeTimeout}") Long writeTimeout,
                                 @Value("${http.readTimeout}") Long readTimeout,
                                 ObjectMapper objectMapper) {
        this.connectionTimeout = connectionTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.objectMapper = objectMapper;
    }


    @PostMapping(value = "/endpoint-data-reference")
    public void receiveEdcCallback(@RequestBody EndpointDataReference dataReference) throws JsonProcessingException {
        var contractAgreementId = dataReference.getProperties().get("cid");
        logger.info("EdcCallbackController [receiveEdcCallback] callBackId:{}, contractAgreementId:{}", dataReference.getId(), contractAgreementId);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
        String body = objectMapper.writeValueAsString(new CommonResponse("Okhttp3 Working.."));
        Request request = new Request.Builder()
                .url(dataReference.getEndpoint())
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode())
                .post(okhttp3.RequestBody.create(body, MediaType.get("application/json")))
                .build();
        try (var response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                logger.info("###################################");
                logger.info("Transfer Process Has been completed");
                logger.info("###################################");
            } else {
                logger.error("###################################");
                logger.error("Transfet process has been failed.");
                logger.error("###################################");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/test")
    public void test(@RequestBody String data) {
        logger.info("###################################");
        logger.info("Data {} Has Received..", data);
        logger.info("###################################");
    }
}
