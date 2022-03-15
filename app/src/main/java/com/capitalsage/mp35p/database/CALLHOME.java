package com.capitalsage.mp35p.database;

public class CALLHOME {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getMaskedpan() {
        return maskedpan;
    }

    public void setMaskedpan(String maskedpan) {
        this.maskedpan = maskedpan;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getProccode() {
        return proccode;
    }

    public void setProccode(String proccode) {
        this.proccode = proccode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getvendorid() {
        return vendorid;
    }

    public void setvendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getfield62() {
        return fieldab;
    }

    public void setfield62(String fieldab) {
        this.fieldab = fieldab;
    }

    public String getpaymentdetails() {
        return paymentdetails;
    }

    public void setpaymentdetails(String vendorid) {
        this.paymentdetails = vendorid;
    }

    int id;
    String stan;
    String maskedpan;
    String authcode;
    String rrn;
    String amt;
    String datetime;
    String mti;
    String proccode;
    String response;
    String appname;
    String cardname;
    String fieldab;
    String vendorid;
    String paymentdetails;

    public CALLHOME()
    {

    }

    public CALLHOME(int id, String stan, String maskedpan, String amt, String datetime,
                    String mti, String proccode, String appname, String cardname, String paymentdetails)
    {
        this.id= id;
        this.stan = stan;
        this.maskedpan = maskedpan;
        this.amt = amt;
        this.datetime = datetime;
        this.mti = mti;
        this.proccode = proccode;
        this.appname = appname;
        this.cardname = cardname;
        this.paymentdetails = paymentdetails;
    }

    public  CALLHOME(String authcode, String response, String vendorid, String fieldab)
    {
        this.authcode = authcode;
        this.response = response;
        this.vendorid = vendorid;
        this.fieldab = fieldab;
    }
}
