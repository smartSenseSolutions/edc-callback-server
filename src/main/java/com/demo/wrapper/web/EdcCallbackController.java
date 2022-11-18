package com.demo.wrapper.web;

import com.demo.wrapper.model.EndpointDataReference;
import com.demo.wrapper.model.TransferData;
import com.demo.wrapper.repository.SentAssetRepository;
import com.demo.wrapper.service.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdcCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(EdcCallbackController.class);

    private final OkHttpClient client;

    private final CacheService cacheService;

    private final ObjectMapper objectMapper;

    private final SentAssetRepository sentAssetRepository;

    public EdcCallbackController(OkHttpClient client, CacheService cacheService, ObjectMapper objectMapper, SentAssetRepository sentAssetRepository) {
        this.client = client;
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
        this.sentAssetRepository = sentAssetRepository;
    }

    @PostMapping(value = "/endpoint-data-reference")
    public void receiveEdcCallback(@RequestBody EndpointDataReference dataReference) throws JsonProcessingException {
        var contractAgreementId = dataReference.getProperties().get("cid");
        logger.info("EdcCallbackController [receiveEdcCallback] callBackId :: {}\n, contractAgreementId :: {}\n, Endpoint :: {}\n, AuthKey :: {}\n, AuthCode :: {}\n",
                dataReference.getId(), contractAgreementId, dataReference.getEndpoint(), dataReference.getAuthKey(), dataReference.getAuthCode());
        TransferData value = new TransferData();
        value.setDescription("This is testing data.");
        value.setReferenceId("testData");
        value.setContractAgreementId(contractAgreementId);
        String body = objectMapper.writeValueAsString(value);
        Request request = new Request.Builder()
                .url(dataReference.getEndpoint())
                .addHeader(dataReference.getAuthKey(), dataReference.getAuthCode())
                .post(okhttp3.RequestBody.create(body, MediaType.get("application/json")))
                .build();
        try (var response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                logger.info("Transfer Process Has been completed");
//                Optional<SentAssetDetails> data = sentAssetRepository.findById(value.getReferenceId());
//                if (data.isPresent()) {
//                    data.get().setStatus("SENT");
//                    sentAssetRepository.save(data.get());
//                }
            } else {
                logger.error("Transfer process has been failed.");
            }
        } catch (Exception e) {
            String key = "receiveEcCallback_agreementId_" + contractAgreementId;
            CacheService.socketException.put(key, CacheService.socketException.containsKey(key) ? CacheService.socketException.get(key) + 1 : 1);
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/test")
    public void test(@RequestBody String data) {
        logger.info("Data {} Has Received..", data);
    }
}
