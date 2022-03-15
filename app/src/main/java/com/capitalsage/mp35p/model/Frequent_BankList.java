package com.capitalsage.mp35p.model;

public class Frequent_BankList {

    String bankName, bankLogoUrl, bankId, keys, bankCode;


    public Frequent_BankList(String bankName, String bankLogoUrl, String bankId, String keys, String bankCode) {
        this.bankName = bankName;
        this.bankLogoUrl = bankLogoUrl;
        this.bankId = bankId;
        this.keys = keys;
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankLogoUrl() {
        return bankLogoUrl;
    }

    public void setBankLogoUrl(String bankLogoUrl) {
        this.bankLogoUrl = bankLogoUrl;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
