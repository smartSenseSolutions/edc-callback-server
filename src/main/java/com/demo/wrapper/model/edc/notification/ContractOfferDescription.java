package com.demo.wrapper.model.edc.notification;

import com.demo.wrapper.model.edc.policy.Policy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContractOfferDescription {
    private final String offerId;
    private final String assetId;
    private final String policyId;
    private final Policy policy;

    @JsonCreator
    public ContractOfferDescription(@JsonProperty("offerId") String offerId, @JsonProperty("assetId") String assetId, @JsonProperty("policyId") String policyId, @JsonProperty("policy") Policy policy) {
        this.offerId = offerId;
        this.assetId = assetId;
        this.policyId = policyId;
        this.policy = policy;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getPolicyId() {
        return policyId;
    }

    public Policy getPolicy() {
        return policy;
    }
}
