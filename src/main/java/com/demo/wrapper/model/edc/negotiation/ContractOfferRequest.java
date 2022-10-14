package com.demo.wrapper.model.edc.negotiation;

import com.demo.wrapper.model.edc.message.RemoteMessage;
import com.demo.wrapper.model.edc.offer.ContractOffer;

import java.util.Objects;

public class ContractOfferRequest implements RemoteMessage {

    private Type type = Type.COUNTER_OFFER;
    private String protocol;
    private String connectorId;
    private String connectorAddress;
    private String correlationId;
    private ContractOffer contractOffer;

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

    public Type getType() {
        return type;
    }

    public ContractOffer getContractOffer() {
        return contractOffer;
    }

    public enum Type {
        INITIAL,
        COUNTER_OFFER
    }

    public static class Builder {
        private final ContractOfferRequest contractOfferRequest;

        private Builder() {
            contractOfferRequest = new ContractOfferRequest();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder protocol(String protocol) {
            contractOfferRequest.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            contractOfferRequest.connectorId = connectorId;
            return this;
        }

        public Builder connectorAddress(String connectorAddress) {
            contractOfferRequest.connectorAddress = connectorAddress;
            return this;
        }

        public Builder correlationId(String correlationId) {
            contractOfferRequest.correlationId = correlationId;
            return this;
        }

        public Builder contractOffer(ContractOffer contractOffer) {
            contractOfferRequest.contractOffer = contractOffer;
            return this;
        }

        public Builder type(Type type) {
            contractOfferRequest.type = type;
            return this;
        }

        public ContractOfferRequest build() {
            Objects.requireNonNull(contractOfferRequest.protocol, "protocol");
            Objects.requireNonNull(contractOfferRequest.connectorId, "connectorId");
            Objects.requireNonNull(contractOfferRequest.connectorAddress, "connectorAddress");
            Objects.requireNonNull(contractOfferRequest.contractOffer, "contractOffer");
            return contractOfferRequest;
        }
    }
}
