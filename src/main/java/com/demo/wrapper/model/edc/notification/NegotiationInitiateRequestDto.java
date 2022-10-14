package com.demo.wrapper.model.edc.notification;

import org.jetbrains.annotations.NotNull;

public class NegotiationInitiateRequestDto {

    private String connectorAddress;
    private String protocol = "ids-multipart";
    private String connectorId;
    private ContractOfferDescription offer;

    private NegotiationInitiateRequestDto() {

    }

    public String getConnectorAddress() {
        return connectorAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public @NotNull ContractOfferDescription getOffer() {
        return offer;
    }


    public static final class Builder {
        private final NegotiationInitiateRequestDto dto;

        private Builder() {
            dto = new NegotiationInitiateRequestDto();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder connectorAddress(String connectorAddress) {
            dto.connectorAddress = connectorAddress;
            return this;
        }

        public Builder protocol(String protocol) {
            dto.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            dto.connectorId = connectorId;
            return this;
        }

        public Builder offerId(ContractOfferDescription offerId) {
            dto.offer = offerId;
            return this;
        }

        public NegotiationInitiateRequestDto build() {
            return dto;
        }
    }
}

