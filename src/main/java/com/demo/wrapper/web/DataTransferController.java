package com.demo.wrapper.web;

import com.demo.wrapper.config.EDCConfiguration;
import com.demo.wrapper.dao.ReceivedAssetDetails;
import com.demo.wrapper.dao.SentAssetDetails;
import com.demo.wrapper.model.CreateDataRequest;
import com.demo.wrapper.model.DataTransferRequest;
import com.demo.wrapper.model.TransferData;
import com.demo.wrapper.model.edc.notification.TransferId;
import com.demo.wrapper.model.edc.offer.ContractOffer;
import com.demo.wrapper.repository.ReceivedAssetRepository;
import com.demo.wrapper.repository.SentAssetRepository;
import com.demo.wrapper.service.CacheService;
import com.demo.wrapper.service.EDCService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DataTransferController {

    private static final Logger logger = LoggerFactory.getLogger(DataTransferController.class);

    private final EDCService edcService;
    private final ObjectMapper objectMapper;
    private final CacheService cacheService;
    private final SentAssetRepository sentAssetRepository;
    private final ReceivedAssetRepository receivedAssetRepository;

    private final EDCConfiguration edcConfiguration;

    @Autowired
    public DataTransferController(EDCService edcService,
                                  ObjectMapper objectMapper, CacheService cacheService,
                                  SentAssetRepository sentAssetRepository,
                                  ReceivedAssetRepository receivedAssetRepository,
                                  EDCConfiguration edcConfiguration) {
        this.edcService = edcService;
        this.objectMapper = objectMapper;
        this.cacheService = cacheService;
        this.edcConfiguration = edcConfiguration;
        this.sentAssetRepository = sentAssetRepository;
        this.receivedAssetRepository = receivedAssetRepository;
    }

    @PostMapping(value = "/init/transfer/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> initTransferProcess(@RequestBody DataTransferRequest request) throws IOException, InterruptedException {
        edcCatalogNegotiationWithTransferProcess(request);
        return Map.of("message", "Success");
    }

    @GetMapping(value = "/error/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> errorsCount() {
        return CacheService.socketException;
    }

    public void edcCatalogNegotiationWithTransferProcess(DataTransferRequest request) throws IOException, InterruptedException {
        Map<String, String> header = new HashMap<>();
        header.put("x-api-key", edcConfiguration.getAuthkey());
        try {
            logger.info("FETCH_CATALOG :: Init process for contract fetch --> ConsumerUrl :: {},  ProviderIdsUrl :: {}", edcConfiguration.getCatalogUrl(), request.getProviderIdsUrl());
            Optional<ContractOffer> contractOffer = null;
            if (request.isUseCache()) {
                contractOffer = cacheService.getCatalog(request.getAssetId());
            } else {
                contractOffer = edcService.fetchCatalogs(request.getAssetId(), edcConfiguration.getCatalogUrl(), request.getProviderIdsUrl(), header);
                cacheService.putCatalog(request.getAssetId(), contractOffer);
            }
            if (contractOffer == null || contractOffer.isEmpty()) {
                logger.error("No Notification contractOffer found");
                throw new RuntimeException("No notification contract offer found.");
            }
            logger.info("Catalog fetched successfully.");

            logger.info("CONTRACT_NEGOTIATION :: Init process for contract negotiation --> ConsumerUrl :: {}, ProviderIdsUrl :: {}", edcConfiguration.getNegotiationUrl(), request.getProviderIdsUrl());
            String agreementId = edcService.initializeContractNegotiation(edcConfiguration.getNegotiationUrl(), request.getProviderIdsUrl(), contractOffer, header);
            logger.info("Contract negotiation process has been completed and generated agreementId --> {}", agreementId);

            logger.info("TRANSFER_PROCESS :: Init process for transfer data --> ConsumerUrl ::{}, ProviderIdsUrl :: {}", edcConfiguration.getTransferProcessUrl(), request.getProviderIdsUrl());
            TransferId transferId = edcService.initiateTransferProcess(agreementId, contractOffer.get().getAsset().getId(), edcConfiguration.getTransferProcessUrl(), request.getProviderIdsUrl(), header);
            logger.info("TransferProcess has been completed successfully and transferId --> {} for assetId --> {}", transferId.getId(), request.getAssetId());

            SentAssetDetails approved = findSendDataFromId(request.getDataId());
            approved.setStatus("APPROVED");
            approved.setContractAgreementId(agreementId);

            sentAssetRepository.save(approved);
            cacheService.putData(agreementId, request.getData(), approved.getId());

        } catch (Exception e) {
            String key = "edcCatalogNegotiationWithTransferProcess_assetId_" + request.getAssetId();
            CacheService.socketException.put(key, CacheService.socketException.containsKey(key) ? CacheService.socketException.get(key) + 1 : 1);
            throw e;
        }
    }


    private SentAssetDetails findSendDataFromId(String id) {
        Optional<SentAssetDetails> sentAssetDetailOpt = sentAssetRepository.findById(id);
        if (!sentAssetDetailOpt.isPresent()) {
            throw new RuntimeException();
        }
        return sentAssetDetailOpt.get();
    }

    @GetMapping(value = "/received/data/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<ReceivedAssetDetails>> getReceivedAssetDetails() {
        return Map.of("data", receivedAssetRepository.findAll());
    }

    @PostMapping(value = "/create/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public SentAssetDetails createSendData(@RequestBody CreateDataRequest createDataRequest) {
        return sentAssetRepository.save(new SentAssetDetails(null, createDataRequest.getDescription(), "CREATED", new Date()));
    }

    @GetMapping(value = "/sent/data/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<SentAssetDetails>> getSentDataDetails() {
        return Map.of("data", sentAssetRepository.findAll());
    }

    @PostMapping("/create/received/data")
    public ReceivedAssetDetails receiveData(@RequestBody TransferData response) throws JsonProcessingException {
        String data = objectMapper.writeValueAsString(response);
        logger.info("Data {} Has Received..", data);
        return receivedAssetRepository.save(new ReceivedAssetDetails(response.getReferenceId(), response.getContractAgreementId(), response.getDescription(), "RECEIVED", new Date()));
    }
}
