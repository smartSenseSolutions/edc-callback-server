package com.demo.wrapper.model.edc.negotiation;


import com.demo.wrapper.model.edc.message.RemoteMessage;

import java.util.Objects;

public class ContractRejection implements RemoteMessage {

    private String protocol;
    private String connectorId;
    private String connectorAddress;
    private String correlationId; // TODO hand over the contract offer/agreement - not an id?
    private String rejectionReason; // TODO pre-define a set of enums (+ mapping to IDS) ?

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

    public String getRejectionReason() {
        return rejectionReason;
    }

    public static class Builder {
        private final ContractRejection contractRejection;

        private Builder() {
            contractRejection = new ContractRejection();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder protocol(String protocol) {
            contractRejection.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            contractRejection.connectorId = connectorId;
            return this;
        }

        public Builder connectorAddress(String connectorAddress) {
            contractRejection.connectorAddress = connectorAddress;
            return this;
        }

        public Builder correlationId(String correlationId) {
            contractRejection.correlationId = correlationId;
            return this;
        }

        public Builder rejectionReason(String rejectionReason) {
            contractRejection.rejectionReason = rejectionReason;
            return this;
        }

        public ContractRejection build() {
            Objects.requireNonNull(contractRejection.protocol, "protocol");
            Objects.requireNonNull(contractRejection.connectorId, "connectorId");
            Objects.requireNonNull(contractRejection.connectorAddress, "connectorAddress");
            Objects.requireNonNull(contractRejection.correlationId, "correlationId");
            Objects.requireNonNull(contractRejection.rejectionReason, "rejectionReason");
            return contractRejection;
        }
    }
}
