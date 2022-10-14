package com.demo.wrapper.model.edc.negotiation;

import java.util.Arrays;

public enum ContractNegotiationStates {

    UNSAVED(0),
    INITIAL(50),
    REQUESTING(100),
    REQUESTED(200),
    PROVIDER_OFFERING(300),
    PROVIDER_OFFERED(400),
    CONSUMER_OFFERING(500),
    CONSUMER_OFFERED(600),
    CONSUMER_APPROVING(700),
    CONSUMER_APPROVED(800),
    DECLINING(900),
    DECLINED(1000),
    CONFIRMING(1100),
    CONFIRMED(1200),
    ERROR(-1);

    private final int code;

    ContractNegotiationStates(int code) {
        this.code = code;
    }

    public static ContractNegotiationStates from(int code) {
        return Arrays.stream(values()).filter(tps -> tps.code == code).findFirst().orElse(null);
    }

    public int code() {
        return code;
    }

}
