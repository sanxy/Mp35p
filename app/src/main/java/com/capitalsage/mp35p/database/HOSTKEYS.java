package com.capitalsage.mp35p.database;

public class HOSTKEYS {
    int id;
    String num;
    String hostname;
    String hostip;
    Integer hostport;
    Boolean hostssl;
    String hostfrendlyname;
    String remarks;
    String ctmk;
    String encMSK;
    String encSK;
    String encPK;
    String clrMSK;
    String clrSK;
    String clrPK;
    String ctmsdatetime;
    String mid;
    String timeout;
    String currencycode;
    String countrycode;
    String callhome;
    String mnl;
    String mcc;

    public HOSTKEYS()
    {

    }
    //After Profile Download
    public HOSTKEYS(int id, String num, String hostname, String hostip, Integer hostport, Boolean hostssl,
                    String hostfrendlyname, String remarks, String ctmk){
        this.id = id;
        this.hostname = hostname;
        this.num = num;
        this.hostip = hostip;
        this.hostport = hostport;
        this.hostssl = hostssl;
        this.hostfrendlyname = hostfrendlyname;
        this.remarks = remarks;
        this.ctmk = ctmk;
    }

    //After Key Exchange
    public HOSTKEYS(String encMSK, String encSK, String encPK, String clrMSK, String clrSK, String clrPK){
        this.encMSK = encMSK;
        this.encSK = encSK;
        this.encPK = encPK;
        this.clrMSK = clrMSK;
        this.clrSK = clrSK;
        this.clrPK = clrPK;
    }

    //After Parameter Download
    public HOSTKEYS(String ctmsdatetime, String mid, String timeout, String currencycode,
                    String countrycode, String callhome, String mnl, String mcc){
        this.ctmsdatetime = ctmsdatetime;
        this.mid = mid;
        this.timeout = timeout;
        this.currencycode = currencycode;
        this.countrycode = countrycode;
        this.callhome = callhome;
        this.mnl = mnl;
        this.mcc = mcc;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getNum(){
        return this.num;
    }

    public void setNum(String num){
        this.num = num;
    }

    public String gethostname(){
        return this.hostname;
    }

    public void sethostname(String hostname){
        this.hostname = hostname;
    }

    public String gethostip(){
        return this.hostip;
    }

    public void sethostip(String hostip){
        this.hostip = hostip;
    }

    public Integer gethostport(){
        return this.hostport;
    }

    public void sethostport(Integer hostport){
        this.hostport = hostport;
    }

    public Boolean gethostssl(){
        return this.hostssl;
    }

    public void sethostssl(Boolean hostssl){
        this.hostssl = hostssl;
    }

    public String gethostfrendlyname(){
        return this.hostfrendlyname;
    }

    public void sethostfrendlyname(String hostfrendlyname){ this.hostfrendlyname = hostfrendlyname; }

    public String getremarks(){
        return this.remarks;
    }

    public void setremarks(String remarks){
        this.remarks = remarks;
    }

    public String getctmk(){
        return this.ctmk;
    }

    public void setctmk(String ctmk){
        this.ctmk = ctmk;
    }

    public String getencMSK(){
        return this.encMSK;
    }

    public void setencMSK(String encMSK){
        this.encMSK = encMSK;
    }

    public String getencSK(){
        return this.encSK;
    }

    public void setencSK(String encSK){
        this.encSK = encSK;
    }

    public String getencPK(){
        return this.encPK;
    }

    public void setencPK(String encPK){
        this.encPK = encPK;
    }

    public String getclrMSK(){
        return this.clrMSK;
    }

    public void setclrMSK(String clrMSK){
        this.clrMSK = clrMSK;
    }

    public String getclrSK(){
        return this.clrSK;
    }

    public void setclrSK(String clrSK){
        this.clrSK = clrSK;
    }

    public String getclrPK(){
        return this.clrPK;
    }

    public void setclrPK(String clrPK){
        this.clrPK = clrPK;
    }

    public String getctmsdatetime(){
        return this.ctmsdatetime;
    }

    public void setctmsdatetime(String ctmsdatetime){
        this.ctmsdatetime = ctmsdatetime;
    }

    public String getmid(){
        return this.mid;
    }

    public void setmid(String mid){
        this.mid = mid;
    }

    public String gettimeout(){
        return this.timeout;
    }

    public void settimeout(String timeout){
        this.timeout = timeout;
    }

    public String getcurrencycode(){
        return this.currencycode;
    }

    public void setcurrencycode(String currencycode){
        this.currencycode = currencycode;
    }

    public String getcountrycode(){
        return this.countrycode;
    }

    public void setcountrycode(String countrycode){
        this.countrycode = countrycode;
    }

    public String getcallhome(){
        return this.callhome;
    }

    public void setcallhome(String callhome){
        this.callhome = callhome;
    }

    public String getmnl(){
        return this.mnl;
    }

    public void setmnl(String mnl){
        this.mnl = mnl;
    }

    public String getmcc(){
        return this.mcc;
    }

    public void setmcc(String mcc){
        this.mcc = mcc;
    }
}
