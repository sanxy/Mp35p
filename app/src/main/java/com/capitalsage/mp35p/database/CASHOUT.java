package com.capitalsage.mp35p.database;

public class CASHOUT {

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCreditassistfee() {
        return creditassistfee;
    }

    public void setCreditassistfee(String creditassistfee) {
        this.creditassistfee = creditassistfee;
    }

    public String getSuperagentfee() {
        return superagentfee;
    }

    public void setSuperagentfee(String superagentfee) {
        this.superagentfee = superagentfee;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMainamount() {
        return mainamount;
    }

    public void setMainamount(String mainamount) {
        this.mainamount = mainamount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getSupersuperfee() {
        return supersuperfee;
    }

    public void setSupersuperfee(String supersuperfee) {
        this.supersuperfee = supersuperfee;
    }

    int id;
    String tid;
    String creditassistfee;
    String status;
    String superagentfee;
    String amount;
    String mainamount;
    String fee;
    String rrn;
    String supersuperfee;

    public CASHOUT()
    {

    }

}
