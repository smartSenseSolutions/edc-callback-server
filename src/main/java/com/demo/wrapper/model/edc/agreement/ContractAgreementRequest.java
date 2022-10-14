package com.demo.wrapper.model.edc.agreement;

import com.demo.wrapper.model.edc.message.RemoteMessage;
import com.demo.wrapper.model.edc.policy.Policy;

import java.util.Objects;

public class ContractAgreementRequest implements RemoteMessage {

    private String protocol;
    private String connectorId;
    private String connectorAddress;
    private String correlationId;
    private ContractAgreement contractAgreement;
    private Policy policy;

    @Override
    public String getProtocol() {
        return protocol;
    }

    public String getConnectorAddress() {
        return connectorAddress;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public ContractAgreement getContractAgreement() {
        return contractAgreement;
    }

    public Policy getPolicy() {
        return policy;
    }

    public static class Builder {
        private final ContractAgreementRequest contractAgreementRequest;

        private Builder() {
            contractAgreementRequest = new ContractAgreementRequest();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder protocol(String protocol) {
            contractAgreementRequest.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            contractAgreementRequest.connectorId = connectorId;
            return this;
        }

        public Builder connectorAddress(String connectorAddress) {
            contractAgreementRequest.connectorAddress = connectorAddress;
            return this;
        }

        public Builder correlationId(String correlationId) {
            contractAgreementRequest.correlationId = correlationId;
            return this;
        }

        public Builder contractAgreement(ContractAgreement contractAgreement) {
            contractAgreementRequest.contractAgreement = contractAgreement;
            return this;
        }

        public Builder policy(Policy policy) {
            contractAgreementRequest.policy = policy;
            return this;
        }

        public ContractAgreementRequest build() {
            Objects.requireNonNull(contractAgreementRequest.protocol, "protocol");
            Objects.requireNonNull(contractAgreementRequest.connectorId, "connectorId");
            Objects.requireNonNull(contractAgreementRequest.connectorAddress, "connectorAddress");
            Objects.requireNonNull(contractAgreementRequest.contractAgreement, "contractAgreement");
            Objects.requireNonNull(contractAgreementRequest.policy, "policy");
            Objects.requireNonNull(contractAgreementRequest.correlationId, "correlationId");
            return contractAgreementRequest;
        }
    }
}
