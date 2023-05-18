package com.demo.wrapper.service;

import com.demo.wrapper.model.edc.notification.ContractNegotiationDto;
import com.demo.wrapper.model.edc.notification.ContractOfferDescription;
import com.demo.wrapper.model.edc.notification.NegotiationInitiateRequestDto;
import com.demo.wrapper.model.edc.notification.TransferId;
import com.demo.wrapper.model.edc.offer.ContractOffer;
import com.demo.wrapper.model.edc.transfer.DataAddress;
import com.demo.wrapper.model.edc.transfer.TransferRequestDto;
import com.demo.wrapper.model.edc.transfer.TransferType;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class EDCService {

    private static final Logger logger = LoggerFactory.getLogger(EDCService.class);
    public static final String ASSET_TYPE_PROPERTY_NAME = "assetId";
    public static final MediaType JSON = MediaType.get("application/json");
    private final HttpCallService httpCallService;

    private final ObjectMapper objectMapper;

    @Autowired
    public EDCService(HttpCallService httpCallService,
                      ObjectMapper objectMapper) {
        this.httpCallService = httpCallService;
        this.objectMapper = objectMapper;
    }

    public Optional<ContractOffer> fetchCatalogs(String assetId, String catalogUrl, String providerIdsUrl, Map<String, String> header) throws IOException {
        var catalog = httpCallService.getCatalogFromProvider(catalogUrl, providerIdsUrl, header);
        if (catalog.getContractOffers().isEmpty()) {
            logger.error("No contract found");
            throw new RuntimeException("Provider has no contract offers for us. Catalog is empty.");
        }
        logger.info("EDCService :: fetchCatalogs --> Fetched Contract Count :: {}", catalog.getContractOffers().size());
        return catalog.getContractOffers().stream()
                .filter(it -> it.getAssetId().equals(assetId))
                .findFirst();
    }

    public String initializeContractNegotiation(String contractNegotiationUrl, String providerConnectorUrl, Optional<ContractOffer> contractOffer, Map<String, String> header) throws InterruptedException, IOException {
        var contractOfferDescription = new ContractOfferDescription(contractOffer.get().getId(), contractOffer.get().getAsset().getId(), null, contractOffer.get().getPolicy());
        var negotiationRequest = NegotiationInitiateRequestDto.Builder.newInstance()
                .offerId(contractOfferDescription).connectorId("provider").connectorAddress(providerConnectorUrl)
                .protocol("ids-multipart").build();
        logger.info("EDCService :: initializeContractNegotiation --> offerId :{}, assetId:{}", contractOffer.get().getId(), contractOffer.get().getAsset().getId());
        var negotiationId = initiateNegotiation(negotiationRequest, contractNegotiationUrl, header);
        ContractNegotiationDto negotiation = null;
        while (negotiation == null || !negotiation.getState().equals("CONFIRMED")) {
            logger.info("EDCService :: initializeContractNegotiation --> waiting for contract to get confirmed");
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            ScheduledFuture<ContractNegotiationDto> scheduledFuture =
                    scheduler.schedule(() -> {
                        var url = contractNegotiationUrl + "/" + negotiationId;
                        var request = new Request.Builder().url(url);
                        header.forEach(request::addHeader);
                        logger.info("EDCService :: initializeContractNegotiation --> Find status of negotiation process by negotiationId {}", negotiationId);
                        return (ContractNegotiationDto) httpCallService.sendRequest(request.build(), ContractNegotiationDto.class);
                    }, 2000, TimeUnit.MILLISECONDS);
            try {
                negotiation = scheduledFuture.get();
                scheduler.shutdown();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } finally {
                if (!scheduler.isShutdown()) {
                    scheduler.shutdown();
                }
            }
        }
        return negotiation.getContractAgreementId();
    }

    private String initiateNegotiation(NegotiationInitiateRequestDto contractOfferRequest, String contractNegotiationUrl, Map<String, String> headers) throws IOException {
        var requestBody = RequestBody.create(objectMapper.writeValueAsString(contractOfferRequest), JSON);
        var request = new Request.Builder().url(contractNegotiationUrl).post(requestBody);
        headers.forEach(request::addHeader);
        request.build();
        TransferId negotiationId = (TransferId) httpCallService.sendRequest(request.build(), TransferId.class);
        logger.info("EDCService :: initiateNegotiation --> Generated Negotiation Id :{}", negotiationId.getId());
        return negotiationId.getId();
    }


    public TransferId initiateTransferProcess(String agreementId, String assetId, String consumerTransferUrl, String providerIdsUrl, Map<String, String> headers) throws IOException {
        DataAddress dataDestination = DataAddress.Builder.newInstance().type("HttpProxy").build();
        TransferType transferType = TransferType.Builder.transferType().contentType("application/octet-stream").isFinite(true).build();
        TransferRequestDto transferRequest = TransferRequestDto.Builder.newInstance()
                .assetId(assetId).contractId(agreementId).connectorId("provider").connectorAddress(providerIdsUrl)
                .protocol("ids-multipart").dataDestination(dataDestination).managedResources(false).transferType(transferType)
                .build();

        var requestBody = RequestBody.create(objectMapper.writeValueAsString(transferRequest), JSON);
        var request = new Request.Builder().url(consumerTransferUrl).post(requestBody);
        headers.forEach(request::addHeader);
        logger.info("EDCService :: initiateTransferProcess --> agreementId : {}, assetId : {}, consumerUrl : {}, providerIdsUrl : {}", agreementId, assetId, consumerTransferUrl, providerIdsUrl);
        return (TransferId) httpCallService.sendRequest(request.build(), TransferId.class);
    }


}
