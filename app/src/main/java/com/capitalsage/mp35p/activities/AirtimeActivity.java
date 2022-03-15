package com.capitalsage.mp35p.activities;

import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.database.DATABASEHANDLER;
import com.capitalsage.mp35p.database.HOSTKEYS;
import com.capitalsage.mp35p.utils.ProfileParser;
import com.cottacush.android.currencyedittext.CurrencyEditText;

import java.util.List;


public class AirtimeActivity extends Activity {

    private static final String TAG = AirtimeActivity.class.getSimpleName();

    String[] serviceNames = {"Choose a network", "MTN", "AIRTEL", "GLO", "9MOBILE"};

    Spinner spinnerSelectService;
    EditText etPhoneNumber;
    String sService = "", sPhoneNumber, sAmount;
    private String curr;
    CardView mBtnConfirm;
    CurrencyEditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);

        etPhoneNumber = findViewById(R.id.edit_text_phone_number);
        etAmount = findViewById(R.id.edit_text_amount);
        mBtnConfirm = findViewById(R.id.vtu_send_now);
        spinnerSelectService = findViewById(R.id.spinner_select_service);

        curr = "₦";
        //Creating the ArrayAdapter instance having the service list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerSelectService.setAdapter(adapter);

        spinnerSelectService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sService = serviceNames[position];
                if (sService.equals("MTN")){
                    ProfileParser.vtuprovider = "MTNVTU";
                }else if (sService.equals("AIRTEL")){
                    ProfileParser.vtuprovider = "AIRTELVTU";
                }else if (sService.equals("GLO")){
                    ProfileParser.vtuprovider = "GLOVTU";
                }else if (sService.equals("9MOBILE")){
                    ProfileParser.vtuprovider = "9MOBILEVTU";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        etAmount.setCurrencySymbol(curr, true);

        findViewById(R.id.back_airtime).setOnClickListener(view -> finish());

        mBtnConfirm.setOnClickListener(view -> {
            hideSoftKeyboard(AirtimeActivity.this);

            if (validate()) {
                if (sService.equals("Choose a network")) {
                    Toast.makeText(getApplicationContext(), "Kindly choose a network", Toast.LENGTH_SHORT).show();
                    return;
                }


            }


        });


    }

    private boolean validate() {
        boolean valid = true;

        sPhoneNumber = etPhoneNumber.getText().toString().trim();
        double amt = etAmount.getNumericValue();
        sAmount = String.valueOf(amt);

        if (sPhoneNumber.isEmpty() || sPhoneNumber.length() < 10) {
            etPhoneNumber.setError("at least 11 characters");
            valid = false;
        } else {
            etPhoneNumber.setError(null);
        }

        if (sAmount.isEmpty()) {
            etAmount.setError("amount cannot be empty");
            valid = false;
        } else {
            etAmount.setError(null);
        }

        return valid;
    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View currentFocus = activity.getCurrentFocus();

        if (inputMethodManager != null) {
            IBinder windowToken = activity.getWindow().getDecorView().getRootView().getWindowToken();
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);

            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }

    }

    Boolean getHostDetails(String txn) {
        String hos = "";
        Log.i(TAG, "PRESSED 2: " + txn);
        String check = txn + "###host1";
        if(ProfileParser.hostarray.toLowerCase().contains(check.toLowerCase()))
            hos = "HOSTA";
        else
            hos = "HOSTB";
        Log.i(TAG, "PRESSED 3: " + hos);
        ProfileParser.hostToUse = hos;
        DATABASEHANDLER db = new DATABASEHANDLER(AirtimeActivity.this);
        List<HOSTKEYS> host = db.getAllHost(hos);
        Log.i(TAG, "GETTING DETAILS FOR: " + hos);
        for (HOSTKEYS cn : host) {
            Log.i(TAG, "NUMBER: " + cn.getNum());
            Log.i(TAG, "HOSTNAME: " + cn.gethostname());
            Log.i(TAG, "HOSTIP: " + cn.gethostip());
            ProfileParser.fhostip = cn.gethostip();
            Log.i(TAG, "HOSTPORT: " + cn.gethostport());
            ProfileParser.fhostport = cn.gethostport().toString();
            Log.i(TAG, "SSL: " + cn.gethostssl());
            ProfileParser.fhostssl = cn.gethostssl().toString();
            Log.i(TAG, "FRIENDLYNAME: " + cn.gethostfrendlyname());
            ProfileParser.fhostfriendlyname = cn.gethostfrendlyname();
            Log.i(TAG, "REMARKS: " + cn.getremarks());
            Log.i(TAG, "CTMK: " + cn.getctmk());
            ProfileParser.fhostctmk = cn.getctmk();
            Log.i(TAG, "ENCMSK: " + cn.getencMSK());
            ProfileParser.fhostencmsk = cn.getencMSK();
            Log.i(TAG, "ENCSK: " + cn.getencSK());
            ProfileParser.fhostencsk = cn.getencSK();
            Log.i(TAG, "ENCPK: " + cn.getencPK());
            ProfileParser.fhostencpk = cn.getencPK();
            Log.i(TAG, "CLRMSK: " + cn.getclrMSK());
            ProfileParser.fhostclrmsk = cn.getclrMSK();
            Log.i(TAG, "CLRSK: " + cn.getclrSK());
            ProfileParser.fhostclrsk = cn.getclrSK();
            Log.i(TAG, "CLRPK: " + cn.getclrPK());
            ProfileParser.fhostclrpk = cn.getclrPK();
            Log.i(TAG, "CTMSDATETIME: " + cn.getctmsdatetime());
            ProfileParser.fhostmid = cn.getmid();
            Log.i(TAG, "MID: " + cn.getmid());
            ProfileParser.fhostclrpk = cn.getclrPK();
            Log.i(TAG, "TIMEOUT: " + cn.gettimeout());
            Log.i(TAG, "CURRENCYCODE: " + cn.getcurrencycode());
            ProfileParser.fhostcurrcode = cn.getcurrencycode();
            Log.i(TAG, "COUNTRYCODE: " + cn.getcurrencycode());
            Log.i(TAG, "CALLHOME: " + cn.getcallhome());
            Log.i(TAG, "MNL: " + cn.getmnl());
            ProfileParser.fhostmnl = cn.getmnl();
            Log.i(TAG, "MCC: " + cn.getmcc());
            ProfileParser.fhostmcc = cn.getmcc();
            Log.i(TAG, "PRESSED 4:A " + ProfileParser.fhostip);
            Log.i(TAG, "PRESSED 5:A " + ProfileParser.fhostport);
            Log.i(TAG, "PRESSED 6:A " + ProfileParser.fhostssl);
            Log.i(TAG, "PRESSED 7:A " + ProfileParser.fhostfriendlyname);

            Log.i(TAG, "IP: " + ProfileParser.fhostip);
            Log.i(TAG, "PORT: " + ProfileParser.fhostport);
            Log.i(TAG, "SSL: " + ProfileParser.fhostssl);

            if(ProfileParser.fhostencmsk == null || ProfileParser.fhostencsk == null ||
                    ProfileParser.fhostencpk == null || ProfileParser.fhostclrmsk == null ||
                    ProfileParser.fhostclrsk == null || ProfileParser.fhostclrpk == null ||
                    ProfileParser.fhostip == null || ProfileParser.fhostport == null
                    || ProfileParser.fhostssl == null || ProfileParser.fhostmnl == null
                    || ProfileParser.fhostmid == null)
            {
                return gotoHome();
            }else
            {
                return true;
            }
        }
        return false;
    }

    Boolean gotoHome() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AirtimeActivity.this);
        builder1.setCancelable(false);
        builder1.setMessage("KEYS NOT LOADED...");

        builder1.setPositiveButton(
                "DOWNLOAD KEYS",
                (dialog, id) -> {
                    dialog.cancel();

                    Intent intent = new Intent();
                    intent.setClass(AirtimeActivity.this, ProfileDownload.class);
                    startActivity(intent);
                    finish();
                });

        builder1.setNegativeButton(
                "CANCEL",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();
        return false;
    }


    public static boolean parser(String rule, String amt) {
        int f = rule.indexOf("-");
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if (Double.parseDouble(amt) <= Double.parseDouble(sR)) {
            ProfileParser.creditassistfee = fee;
            System.out.println("CREDIT ASSIST FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRule(String str, String amount) {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex == -1)
                break;
            if (parser(str.substring(previous, lastIndex), amount))
                return;

            if (lastIndex != -1) {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    public static boolean parserSA(String rule, String amt) {
        int f = rule.indexOf("-");
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if (Double.parseDouble(amt) <= Double.parseDouble(sR)) {
            ProfileParser.superagentfee = fee;
            System.out.println("SUPER AGENT FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRuleSA(String str, String amount) {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex == -1)
                break;
            if (parserSA(str.substring(previous, lastIndex), amount))
                return;

            if (lastIndex != -1) {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    public static boolean parserSS(String rule, String amt) {
        int f = rule.indexOf("-");
        String fR = rule.substring(0, f);

        int s = rule.indexOf("=", f);
        String sR = rule.substring(f + 1, s);

        String fee = rule.substring(s + 1);

        if (Double.parseDouble(amt) <= Double.parseDouble(sR)) {
            ProfileParser.supersuperfee = fee;
            System.out.println("SUPER SUPER FEE IS: " + fee);
            return true;
        }
        return false;
    }

    public void ProcessRuleSS(String str, String amount) {
        String findStr = "###";
        int lastIndex = 0;
        int count = 0;
        int previous = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex == -1)
                break;
            if (parserSS(str.substring(previous, lastIndex), amount))
                return;

            if (lastIndex != -1) {
                count++;
                previous = lastIndex + 3;
                lastIndex += findStr.length();
            }
        }
    }

    void checkRules() {
        Log.i(TAG, "CHECK TXN FIXED: " + ProfileParser.txnNumber);
        if (Integer.parseInt(ProfileParser.txnNumber) == 2) {
//            Intent intent3 = new Intent(AirtimeActivity.this, EmvPayActivity.class);
//            intent3.putExtra("transaction", "CASH WITHDRAWAL");
//            startActivity(intent3);
//            requireActivity().finish();
        }
    }

    void checkRulesPercentage() {
        Log.i(TAG, "CHECK TXN PERCENTAGE: " + ProfileParser.txnNumber);
        if (Integer.parseInt(ProfileParser.txnNumber) == 2) {
//            Intent intent3 = new Intent(AirtimeActivity.this, Emvpay.class);
//            intent3.putExtra("transaction", "CASH WITHDRAWAL");
//            startActivity(intent3);
        } else if (Integer.parseInt(ProfileParser.txnNumber) == 3) {
//            Intent intent3 = new Intent(AirtimeActivity.this, AccountOptions.class);
//            intent3.putExtra("transaction", "CASH DEPOSIT");
//            startActivity(intent3);
//            requireActivity().finish();
        } else if (Integer.parseInt(ProfileParser.txnNumber) == 4) {
//            Intent intent3 = new Intent(AirtimeActivity.this, AccountOptions.class);
//            intent3.putExtra("transaction", "CASH TRANSFER");
//            startActivity(intent3);
//            requireActivity().finish();
        }
    }

    void checkForVtuMax() {
        if (Double.parseDouble(ProfileParser.totalAmount) < Double.parseDouble("50.00")) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AirtimeActivity.this);
            builder1.setCancelable(false);
            builder1.setMessage("MINIMUM AMOUNT IS NGN 50.00");

            builder1.setPositiveButton(
                    "RETRY", (dialog, id) -> {
                        dialog.cancel();

//                            Intent intent = new Intent();
//                            intent.setClass(requireActivity(), MainActivity.class);
//                            startActivity(intent);
//                            requireActivity().finish();
                    });

            builder1.setNegativeButton(
                    "CANCEL",
                    (dialog, id) -> {
                        dialog.cancel();
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
//            ((MainActivity) requireActivity()).goToEmvActivity();

        }
    }

    void checkForMax() {
        if (Double.parseDouble(ProfileParser.Amount) > Double.parseDouble(ProfileParser.maxamount)) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AirtimeActivity.this);
            builder1.setCancelable(false);
            builder1.setMessage("MAXIMUM AMOUNT EXCEEDED");

            builder1.setPositiveButton(
                    "DOWNLOAD PROFILE",
                    (dialog, id) -> {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(AirtimeActivity.this, ProfileDownload.class);
                        startActivity(intent);
                       finish();
                    });

            builder1.setNegativeButton(
                    "CANCEL",
                    (dialog, id) -> {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(AirtimeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            if (Integer.parseInt(ProfileParser.txnNumber) == 1) {
                ProfileParser.totalAmount = ProfileParser.Amount;
//                Intent intent3 = new Intent(AirtimeActivity.this, Emvpay.class);
//                startActivity(intent3);
//                requireActivity().finish();
            } else if (Integer.parseInt(ProfileParser.txnNumber) == 2) {
                //Withdrawal
                ProfileParser.totalAmount = ProfileParser.Amount;
                ProfileParser.Fee = "0.00";
                ProcessRule(ProfileParser.creditassistfeerule, ProfileParser.totalAmount);
                ProcessRuleSA(ProfileParser.superagentfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.superpercentage, ProfileParser.totalAmount);
                checkRulesPercentage();
            } else if (Integer.parseInt(ProfileParser.txnNumber) == 3) {
                //Deposit
                ProfileParser.totalAmount = ProfileParser.Amount;
                ProfileParser.Fee = "0.00";
                ProcessRule(ProfileParser.ctransferrule, ProfileParser.totalAmount);
                ProcessRuleSA(ProfileParser.satfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.wtsupersuper, ProfileParser.totalAmount);
                checkRules();
            } else if (Integer.parseInt(ProfileParser.txnNumber) == 4) {
                //Transfer
                ProfileParser.totalAmount = ProfileParser.Amount;
                ProfileParser.Fee = "0.00";
                ProcessRule(ProfileParser.ctransferrule, ProfileParser.totalAmount);
                ProcessRuleSA(ProfileParser.satfeerule, ProfileParser.totalAmount);
                ProcessRuleSS(ProfileParser.wtsupersuper, ProfileParser.totalAmount);
                checkRules();
            }
            else if (Integer.parseInt(ProfileParser.txnNumber) == 5) {
                //VTU
                ProfileParser.totalAmount = ProfileParser.Amount;
                checkForVtuMax();
            } else if (Integer.parseInt(ProfileParser.txnNumber) == 6) {
                //Data
                ProfileParser.totalAmount = ProfileParser.Amount;
//                Intent intent3 = new Intent(AirtimeActivity.this, Emvpay.class);
//                startActivity(intent3);
//                requireActivity().finish();
            } else if (Integer.parseInt(ProfileParser.txnNumber) == 7) {
                ProfileParser.totalAmount = ProfileParser.Amount;
//                Intent intent3 = new Intent(AirtimeActivity.this, Validation.class);
//                startActivity(intent3);
//                requireActivity().finish();
            }
        }
    }

}