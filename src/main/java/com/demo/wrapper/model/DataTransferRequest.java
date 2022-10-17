package com.demo.wrapper.model;

import lombok.Data;

@Data
public class DataTransferRequest {

    private String providerIdsUrl;
    private String assetId;
    private String data;
    private boolean useCache;
    private String dataId;

}
