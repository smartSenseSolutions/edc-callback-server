package com.demo.wrapper.model;

public class TransferData {

    public TransferData() {
    }

    private String description;
    private String referenceId;
    private String contractAggrementId;

    public TransferData(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getContractAggrementId() {
        return contractAggrementId;
    }

    public void setContractAggrementId(String contractAggrementId) {
        this.contractAggrementId = contractAggrementId;
    }
}
