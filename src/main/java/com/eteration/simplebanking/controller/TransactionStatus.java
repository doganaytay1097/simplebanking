package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.TransactionResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatus {


    @JsonIgnore
    private TransactionResult result;

    private String approvalCode;

    public TransactionStatus() { }

    public TransactionStatus(TransactionResult result, String approvalCode) {
        this.result = result;
        this.approvalCode = approvalCode;
    }


    @JsonProperty("status")
    public String getStatus() {
        return result != null ? result.name() : null;
    }

    public TransactionResult getResult() {
        return result;
    }

    public void setResult(TransactionResult result) {
        this.result = result;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
}
