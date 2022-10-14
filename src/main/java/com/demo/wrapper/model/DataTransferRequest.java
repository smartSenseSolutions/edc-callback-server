package com.demo.wrapper.model;

public class DataTransferRequest {

    private String providerIdsUrl;
    private String assetId;
    private String data;
    private boolean useCache;

    public String getProviderIdsUrl() {
        return providerIdsUrl;
    }

    public void setProviderIdsUrl(String providerIdsUrl) {
        this.providerIdsUrl = providerIdsUrl;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }
}
