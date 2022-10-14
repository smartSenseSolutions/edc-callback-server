package com.demo.wrapper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "organization")
public class EDCConfiguration {

    private String edcHost;
    private String catalogEndpoint;
    private String transferEndpoint;
    private String negotiationEndpoint;
    private String authkey;

    public String getEdcHost() {
        return edcHost;
    }

    public void setEdcHost(String edcHost) {
        this.edcHost = edcHost;
    }

    public String getCatalogEndpoint() {
        return catalogEndpoint;
    }

    public void setCatalogEndpoint(String catalogEndpoint) {
        this.catalogEndpoint = catalogEndpoint;
    }

    public String getTransferEndpoint() {
        return transferEndpoint;
    }

    public void setTransferEndpoint(String transferEndpoint) {
        this.transferEndpoint = transferEndpoint;
    }

    public String getNegotiationEndpoint() {
        return negotiationEndpoint;
    }

    public void setNegotiationEndpoint(String negotiationEndpoint) {
        this.negotiationEndpoint = negotiationEndpoint;
    }

    public String getAuthkey() {
        return authkey;
    }

    public void setAuthkey(String authkey) {
        this.authkey = authkey;
    }

    public String getCatalogUrl() {
        return edcHost + catalogEndpoint;
    }

    public String getNegotiationUrl() {
        return edcHost + negotiationEndpoint;
    }

    public String getTransferProcessUrl() {
        return edcHost + transferEndpoint;
    }

}
