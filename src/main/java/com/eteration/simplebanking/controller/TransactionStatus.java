package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.TransactionResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatus {

    // Enum’u içeride "result" olarak tutuyoruz
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

}
