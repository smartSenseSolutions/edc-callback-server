package com.demo.wrapper.dao;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sent_asset_details")
public class SentAssetDetails {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;
    @Column(name = "contractAgreementId")
    private String contractAgreementId;
    @Column(name = "data")
    private String data;
    @Column(name = "status")
    private String status;
    @Column(name = "created_date")
    private Date createdAt = new Date();

    public SentAssetDetails() {
    }

    public SentAssetDetails(String contractAgreementId, String data, String status, Date createdAt) {
        this.contractAgreementId = contractAgreementId;
        this.data = data;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractAgreementId() {
        return contractAgreementId;
    }

    public void setContractAgreementId(String contractAgreementId) {
        this.contractAgreementId = contractAgreementId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
