package com.demo.wrapper.model.edc.agreement;

import com.demo.wrapper.model.edc.policy.Policy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@JsonDeserialize(builder = ContractAgreement.Builder.class)
public class ContractAgreement {

    private final String id;
    private final String providerAgentId;
    private final String consumerAgentId;
    private final long contractSigningDate;
    private final long contractStartDate;
    private final long contractEndDate;
    private final String assetId;
    private final Policy policy;

    private ContractAgreement(@NotNull String id,
                              @NotNull String providerAgentId,
                              @NotNull String consumerAgentId,
                              long contractSigningDate,
                              long contractStartDate,
                              long contractEndDate,
                              @NotNull Policy policy,
                              @NotNull String assetId) {
        this.id = Objects.requireNonNull(id);
        this.providerAgentId = Objects.requireNonNull(providerAgentId);
        this.consumerAgentId = Objects.requireNonNull(consumerAgentId);
        this.contractSigningDate = contractSigningDate;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.assetId = Objects.requireNonNull(assetId);
        this.policy = Objects.requireNonNull(policy);
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getProviderAgentId() {
        return providerAgentId;
    }

    @NotNull
    public String getConsumerAgentId() {
        return consumerAgentId;
    }

    public long getContractSigningDate() {
        return contractSigningDate;
    }

    public long getContractStartDate() {
        return contractStartDate;
    }

    public long getContractEndDate() {
        return contractEndDate;
    }

    @NotNull
    public String getAssetId() {
        return assetId;
    }

    public Policy getPolicy() {
        return policy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerAgentId, consumerAgentId, contractSigningDate, contractStartDate, contractEndDate, assetId, policy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractAgreement that = (ContractAgreement) o;
        return contractSigningDate == that.contractSigningDate && contractStartDate == that.contractStartDate && contractEndDate == that.contractEndDate &&
                Objects.equals(id, that.id) && Objects.equals(providerAgentId, that.providerAgentId) && Objects.equals(consumerAgentId, that.consumerAgentId) &&
                Objects.equals(assetId, that.assetId) && Objects.equals(policy, that.policy);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private String id;
        private String providerAgentId;
        private String consumerAgentId;
        private long contractSigningDate;
        private long contractStartDate;
        private long contractEndDate;
        private String assetId;
        private Policy policy;

        private Builder() {
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder providerAgentId(String providerAgentId) {
            this.providerAgentId = providerAgentId;
            return this;
        }

        public Builder consumerAgentId(String consumerAgentId) {
            this.consumerAgentId = consumerAgentId;
            return this;
        }

        public Builder contractSigningDate(long contractSigningDate) {
            this.contractSigningDate = contractSigningDate;
            return this;
        }

        public Builder contractStartDate(long contractStartDate) {
            this.contractStartDate = contractStartDate;
            return this;
        }

        public Builder contractEndDate(long contractEndDate) {
            this.contractEndDate = contractEndDate;
            return this;
        }

        public Builder assetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public Builder policy(Policy policy) {
            this.policy = policy;
            return this;
        }

        public ContractAgreement build() {
            return new ContractAgreement(id, providerAgentId, consumerAgentId, contractSigningDate, contractStartDate, contractEndDate, policy, assetId);
        }
    }
}
