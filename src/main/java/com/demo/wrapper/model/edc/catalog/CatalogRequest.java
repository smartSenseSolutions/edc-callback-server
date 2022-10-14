package com.demo.wrapper.model.edc.catalog;

import com.demo.wrapper.model.edc.message.RemoteMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@JsonDeserialize(builder = CatalogRequest.Builder.class)
public class CatalogRequest implements RemoteMessage {

    private final String protocol;
    private final String connectorId;
    private final String connectorAddress;

    private CatalogRequest(@NotNull String protocol, @NotNull String connectorId, @NotNull String connectorAddress) {
        this.protocol = protocol;
        this.connectorId = connectorId;
        this.connectorAddress = connectorAddress;
    }

    @Override
    @NotNull
    public String getProtocol() {
        return protocol;
    }

    @NotNull
    public String getConnectorId() {
        return connectorId;
    }

    @NotNull
    public String getConnectorAddress() {
        return connectorAddress;
    }

    public static class Builder {
        private String protocol;
        private String connectorId;
        private String connectorAddress;

        private Builder() {
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            this.connectorId = connectorId;
            return this;
        }

        public Builder connectorAddress(String connectorAddress) {
            this.connectorAddress = connectorAddress;
            return this;
        }

        public CatalogRequest build() {
            Objects.requireNonNull(protocol, "protocol");
            Objects.requireNonNull(connectorId, "connectorId");
            Objects.requireNonNull(connectorAddress, "connectorAddress");

            return new CatalogRequest(protocol, connectorId, connectorAddress);
        }
    }
}
