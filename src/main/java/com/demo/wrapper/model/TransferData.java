package com.demo.wrapper.model;

public class TransferData {

    public TransferData() {
    }

    private String description;
    private String referenceId;
    private String contractAgreementId;

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

    public String getContractAgreementId() {
        return contractAgreementId;
    }

    public void setContractAgreementId(String contractAgreementId) {
        this.contractAgreementId = contractAgreementId;
    }
}
