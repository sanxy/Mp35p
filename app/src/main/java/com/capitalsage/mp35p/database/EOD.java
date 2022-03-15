package com.capitalsage.mp35p.database;


public class EOD {
    int id;
    String mti;
    String localdatetime;
    String track2;
    String proccode;
    String amount;
    String rrn;
    String panseqnum;
    String entrymode;
    String posdatacode;
    String stan;
    String authcode;
    String condcode;
    String respcode;
    String acquirerid;
    String receiptno;
    String txntype;
    String rdate;
    String rtime;
    String appname;
    String expdate;
    String maskedpan;
    String holdername;
    String aid;
    String cashback;
    String total;
    String paymentdetails;
    String timestamp;
    String date;

    public EOD()
    {

    }

    public EOD(int id, String mti, String localdatetime, String track2, String proccode, String amount,
               String rrn, String panseqnum, String entrymode, String posdatacode, String stan,
               String condcode, String acquirerid, String timestamp,
               String date, String txntype, String appname,
               String expdate, String maskedpan, String holdername, String aid, String cashback, String total){
        this.id = id;
        this.mti = mti;
        this.localdatetime = localdatetime;
        this.track2 = track2;
        this.proccode = proccode;
        this.amount = amount;
        this.rrn = rrn;
        this.panseqnum = panseqnum;
        this.entrymode = entrymode;
        this.posdatacode = posdatacode;
        this.stan = stan;
        this.condcode = condcode;
        this.acquirerid = acquirerid;
        this.timestamp = timestamp;
        this.date = date;
        this.txntype = txntype;
        this.appname = appname;
        this.expdate = expdate;
        this.maskedpan = maskedpan;
        this.holdername = holdername;
        this.aid = aid;
        this.cashback = cashback;
        this.total = total;
    }

    public EOD(String authcode, String respcode, String receiptno, String rdate, String rtime, String paymentdetails){
        this.authcode = authcode;
        this.respcode = respcode;
        this.receiptno = receiptno;
        this.rdate = rdate;
        this.rtime = rtime;
        this.paymentdetails = paymentdetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public String getLocaldatetime() {
        return localdatetime;
    }

    public void setLocaldatetime(String localdatetime) {
        this.localdatetime = localdatetime;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getProccode() {
        return proccode;
    }

    public void setProccode(String proccode) {
        this.proccode = proccode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getPanseqnum() {
        return panseqnum;
    }

    public void setPanseqnum(String panseqnum) {
        this.panseqnum = panseqnum;
    }

    public String getEntrymode() {
        return entrymode;
    }

    public void setEntrymode(String entrymode) {
        this.entrymode = entrymode;
    }

    public String getPosdatacode() {
        return posdatacode;
    }

    public void setPosdatacode(String posdatacode) {
        this.posdatacode = posdatacode;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getCondcode() {
        return condcode;
    }

    public void setCondcode(String condcode) {
        this.condcode = condcode;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getAcquirerid() {
        return acquirerid;
    }

    public void setAcquirerid(String acquirerid) {
        this.acquirerid = acquirerid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public String getTxntype() {
        return txntype;
    }

    public void setTxntype(String txntype) {
        this.txntype = txntype;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMaskedpan() {
        return maskedpan;
    }

    public void setMaskedpan(String maskedpan) {
        this.maskedpan = maskedpan;
    }

    public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaymentdetails() {
        return paymentdetails;
    }

    public void setPaymentdetails(String paymentdetails) {
        this.paymentdetails = paymentdetails;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


