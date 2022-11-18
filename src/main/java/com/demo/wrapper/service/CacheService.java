package com.demo.wrapper.service;

import com.demo.wrapper.model.TransferData;
import com.demo.wrapper.model.edc.offer.ContractOffer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CacheService {

    private static Map<String, TransferData> contractAgreementAndDataMap = new HashMap<>();
    private static Map<String, Optional<ContractOffer>> assetAndCatalogMap = new HashMap<>();
    public static Map<String, Integer> socketException = new HashMap<>();


    public void putCatalog(String assetId, Optional<ContractOffer> catalog) {
        assetAndCatalogMap.put(assetId, catalog);
    }

    public Optional<ContractOffer> getCatalog(String assetId) {
        return assetAndCatalogMap.get(assetId);
    }

    public void putData(String agreementId, String data, String referenceId) {
        TransferData transferData = new TransferData();
        transferData.setDescription(data);
        transferData.setReferenceId(referenceId);
        transferData.setContractAgreementId(agreementId);
        contractAgreementAndDataMap.put(agreementId, transferData);
    }

    public TransferData getData(String agreementId) {
        return contractAgreementAndDataMap.get(agreementId);
    }

}
