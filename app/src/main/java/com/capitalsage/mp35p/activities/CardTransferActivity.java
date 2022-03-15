package com.capitalsage.mp35p.activities;


import static com.capitalsage.mp35p.utils.ProfileParser.BASEURL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.adapter.BankListAdapter;
import com.capitalsage.mp35p.adapter.FrequentBankListAdapter;
import com.capitalsage.mp35p.model.BankList;
import com.capitalsage.mp35p.model.Frequent_BankList;
import com.capitalsage.mp35p.utils.ProfileParser;
import com.capitalsage.mp35p.utils.Storage;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class CardTransferActivity extends Activity 
        implements FrequentBankListAdapter.OnBankListener
{

    private static final String TAG = CardTransferActivity.class.getSimpleName();
    CardView mBtnConfirm, cdAmount;
    TextView tvValidatedName, tvValidateButton, tvAmount;
    EditText etAccountNumber;
    LinearProgressIndicator progressIndicator;
    ArrayList<BankList> items;
    ArrayList<Frequent_BankList> frequent_banklist_items;
    CurrencyEditText etAmount;
    SharedPreferences sharedPreferences;
    String[] typ;
    String[] keys;
    String[] code;
    String[] frequent_bank_typ;
    String[] frequent_bank_keys;
    String[] frequent_bank_code;
    String[] frequent_bank_image;
    private TextView spinnerSelectBank;
    private LinearLayout barrier;
    private TextView disp;
    private BankListAdapter adapter;
    private String curr;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_transfer);

        initFreqBank();
        initUI();


    }

    private void initFreqBank() {
        items = new ArrayList<BankList>();

        frequent_banklist_items = new ArrayList<Frequent_BankList>();

        frequent_bank_typ = new String[9];
        frequent_bank_keys = new String[9];
        frequent_bank_code = new String[9];
        frequent_bank_image = new String[9];

        frequent_bank_typ[2] = "FBN";
        frequent_bank_keys[2] = "011";
        frequent_bank_code[2] = "011151003";
        frequent_bank_image[2] = "first_bank";

        frequent_bank_typ[6] = "UBA";
        frequent_bank_keys[6] = "033";
        frequent_bank_code[6] = "033152048";
        frequent_bank_image[6] = "united_bank_for_africa";

        frequent_bank_typ[0] = "ACCESS";
        frequent_bank_keys[0] = "044";
        frequent_bank_code[0] = "044150291";
        frequent_bank_image[0] = "access_bank";

        frequent_bank_typ[4] = "GTB";
        frequent_bank_keys[4] = "058";
        frequent_bank_code[4] = "058152052";
        frequent_bank_image[4] = "guaranty_trust_bank";

        frequent_bank_typ[3] = "FCMB";
        frequent_bank_keys[3] = "214";
        frequent_bank_code[3] = "214150018";
        frequent_bank_image[3] = "fcmb";

        frequent_bank_typ[8] = "ZENITH";
        frequent_bank_keys[8] = "057";
        frequent_bank_code[8] = "057150013";
        frequent_bank_image[8] = "zenith_bank";


        frequent_bank_typ[1] = "ECO";
        frequent_bank_keys[1] = "050";
        frequent_bank_code[1] = "050150010";
        frequent_bank_image[1] = "ecobank";

        frequent_bank_typ[5] = "UNION";
        frequent_bank_keys[5] = "032";
        frequent_bank_code[5] = "032154568";
        frequent_bank_image[5] = "union_bank";

        frequent_bank_typ[7] = "WEMA";
        frequent_bank_keys[7] = "035";
        frequent_bank_code[7] = "035150103";
        frequent_bank_image[7] = "wema_bank";


        typ = new String[35];
        keys = new String[35];
        code = new String[35];

        typ[0] = "ACCESS BANK NIGERIA LTD";
        keys[0] = "044";
        code[0] = "044150291";

        typ[1] = "CENTRAL BANK OF NIGERIA";
        keys[1] = "001";
        code[1] = "001080032";

        typ[2] = "CORONATION MERCHANT BANK";
        keys[2] = "559";
        code[2] = "559155591";

        typ[3] = "DIAMOND BANK LTD";
        keys[3] = "063";
        code[3] = "063150162";

        typ[4] = "ECOBANK NIGERIA PLC";
        keys[4] = "050";
        code[4] = "050150010";

        typ[5] = "FBNQuest Merchant Bank Limited";
        keys[5] = "060002";
        code[5] = "060002600";

        typ[6] = "FIDELITY BANK PLC";
        keys[6] = "070";
        code[6] = "070150003";

        typ[7] = "FIRST BANK OF NIGERIA PLC";
        keys[7] = "011";
        code[7] = "011151003";

        typ[8] = "FIRST CITY MONUMENT BANK";
        keys[8] = "214";
        code[8] = "214150018";

        typ[9] = "FINATRUST MICROFINANCE BANK";
        keys[9] = "608";
        code[9] = "608155608";

        typ[10] = "FSDH MERCHANT BANK LIMIT";
        keys[10] = "601";
        code[10] = "601155601";

        typ[11] = "GUARANTY TRUST BANK PLC";
        keys[11] = "058";
        code[11] = "058152052";

        typ[12] = "HASAL MICROFINANCE BANK";
        keys[12] = "090121";
        code[12] = "090118509";

        typ[13] = "HERITAGE BANK";
        keys[13] = "030";
        code[13] = "030150014";

        typ[14] = "IBILE MFB";
        keys[14] = "090118";
        code[14] = "090185090";

        typ[15] = "JAIZ BANK PLC";
        keys[15] = "301";
        code[15] = "301080020";

        typ[16] = "KEYSTONE BANK LTD";
        keys[16] = "082";
        code[16] = "082150017";

        typ[17] = "New Prudential Bank";
        keys[17] = "561";
        code[17] = "561155561";

        typ[18] = "NIGERIA INTERNATINAL BANK (CITIBANK)";
        keys[18] = "023";
        code[18] = "023150005";

        typ[19] = "NPF Microfinance Bank";
        keys[19] = "552";
        code[19] = "552155552";

        typ[20] = "PAGA";
        keys[20] = "327";
        code[20] = "327155327";

        typ[21] = "Page MFBank";
        keys[21] = "560";
        code[21] = "560155560";

        typ[22] = "PARALLEX MFB";
        keys[22] = "526";
        code[22] = "526155261";

        typ[23] = "PROVIDUS BANK";
        keys[23] = "101";
        code[23] = "101152019";

        typ[24] = "RAND MERCHANT BANK";
        keys[24] = "502";
        code[24] = "502155502";

        typ[25] = "SKYE BANK PLC";
        keys[25] = "076";
        code[25] = "076151365";

        typ[26] = "STANBIC IBTC BANK PLC";
        keys[26] = "221";
        code[26] = "221159522";

        typ[27] = "STANDARD CHARTERED BANK NIGERIA LTD";
        keys[27] = "068";
        code[27] = "068150015";

        typ[28] = "STERLING BANK PLC";
        keys[28] = "232";
        code[28] = "232150016";

        typ[29] = "SUNTRUST BANK";
        keys[29] = "100";
        code[29] = "100152049";

        typ[30] = "UNION BANK OF NIGERIA PLC";
        keys[30] = "032";
        code[30] = "032154568";

        typ[31] = "UNITY BANK PLC";
        keys[31] = "215";
        code[31] = "215082334";

        typ[32] = "UNITED BANK FOR AFRICA PLC";
        keys[32] = "033";
        code[32] = "033152048";

        typ[33] = "WEMA BANK PLC";
        keys[33] = "035";
        code[33] = "035150103";

        typ[34] = "ZENITH INTERNATIONAL BANK LTD";
        keys[34] = "057";
        code[34] = "057150013";
    }

    private void initUI() {
        sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);

        spinnerSelectBank = findViewById(R.id.spinner_select_bank);
        cdAmount = findViewById(R.id.card_view_amount);
        tvValidatedName = findViewById(R.id.tv_validated_name);

        tvAmount = findViewById(R.id.tv_amount);
        etAccountNumber = findViewById(R.id.account_number);
        barrier = findViewById(R.id.barrier);
        disp = findViewById(R.id.loading_text);
        progressIndicator = findViewById(R.id.progress_bar);
        mBtnConfirm = findViewById(R.id.bank_transfer_send_now);
        tvValidateButton = findViewById(R.id.validate_button);
        etAmount = findViewById(R.id.edit_text_amount);

        if (ProfileParser.curabbr.equals("NGN"))
            curr = "₦";
        else if (ProfileParser.curabbr.equals("USD"))
            curr = "$";
        else if (ProfileParser.curabbr.equals("GBP"))
            curr = "£";
        else if (ProfileParser.curabbr.equals("RMB"))
            curr = "￥";

//        etAmount.setCurrencySymbol(curr, true);

        findViewById(R.id.back_bank_transfer).setOnClickListener(view -> finish());

        mBtnConfirm.setOnClickListener(view -> {

            hideSoftKeyboard(CardTransferActivity.this);
            validateAllDetailsAndProceed();
        });

        spinnerSelectBank.setOnClickListener(v -> selectBank());

        RecyclerView recyclerView =findViewById(R.id.frequent_bank_list_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // at last we are setting adapter to our recycler view.
        FrequentBankListAdapter frequentBankListAdapter = new FrequentBankListAdapter(getApplicationContext(), getFrequentBanks(), this);
        recyclerView.setAdapter(frequentBankListAdapter); // set the Adapter to RecyclerView

        etAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {

                    try {
                        validateAccountName();
                    }catch (Exception e){
                        Log.i(TAG, "Error:: account name is not available"+ e.getMessage());
                        tvValidatedName.setText("validated name appears here");
                    }

                } else {
                    tvValidatedName.setText("validated name appears here");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void validateAccountName() {
        if (spinnerSelectBank.getText().toString().equals(getResources().getString(R.string.select_bank))) {
            Toast.makeText(getApplicationContext(), "Select a bank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etAccountNumber.getText().toString().equals("") ||
                etAccountNumber.getText().toString().length() < 10) {
            Toast.makeText(getApplicationContext(), "Enter account number", Toast.LENGTH_SHORT).show();
            return;
        }

        ProfileParser.destination = etAccountNumber.getText().toString().trim();
        ProfileParser.vtudescription = "KOLOMONI CASH TRANSFER";
        barrier.setVisibility(View.VISIBLE);
        new GetMethodAccountName().execute(BASEURL + "/ctams/processor/validationothers/");
    }

    private void validateAllDetailsAndProceed() {
        if (spinnerSelectBank.getText().toString().equals(getResources().getString(R.string.select_bank))) {
            Toast.makeText(getApplicationContext(), "Select a bank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etAccountNumber.getText().toString().equals("") ||
                etAccountNumber.getText().toString().length() < 10) {
            Toast.makeText(getApplicationContext(), "Enter account number", Toast.LENGTH_SHORT).show();
            return;
        }


        String amt = String.valueOf(etAmount.getNumericValueBigDecimal());
        if (amt.equals(curr) || amt.equals("0")) {
            Toast.makeText(getApplicationContext(), "Kindly enter amount ", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "amount:::: "+ etAmount.getNumericValueBigDecimal());

        if (!tvValidatedName.getText().toString().equalsIgnoreCase("validated name appears here")) {
            // check kyc transfer limit before proceeding with the transaction
            checkKycTransferLimit();

        }

    }

    private void checkKycTransferLimit() {
        String kycLevel = "";
        kycLevel = sharedPreferences.getString(Storage.KYC_LEVEL, "");
        String currentTransferLimit = "", currentWithdrawalLimit = "";

        JSONObject obj = null;
        try {
            String str = sharedPreferences.getString(Storage.KYC_ALL_LEVEL, "");
            obj = new JSONObject(str);
            JSONArray arr = obj.getJSONArray("message");
            NumberFormat numberFormat = new DecimalFormat("#,###.##");

            Log.i(TAG, "arr length: " + arr.length());
            Log.i(TAG, "KycAllLevel: " + str);

            for (int i = 0; i < arr.length(); i++) {

                if (arr.getJSONObject(i).getString("level").equals(kycLevel)) {
                    Log.i(TAG, "Level known: " + arr.getJSONObject(i).getString("level"));

                    currentTransferLimit = arr.getJSONObject(i).getString("max_transfer");
                    currentWithdrawalLimit = arr.getJSONObject(i).getString("max_withdrawal");

                } else if (kycLevel.equals("null")) {
                    Log.i(TAG, "Level unknown: " + arr.getJSONObject(i).getString("level"));

                    //set level default to one
                    currentTransferLimit = "50000";
                    currentWithdrawalLimit = "150000";

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        double amount = 0.0, transferLimit = 0.0;
        amount = etAmount.getNumericValue();
        transferLimit = Double.parseDouble(currentTransferLimit);

        if (amount <= transferLimit) {
            Log.i(TAG, "amount is within:::: ");
            checkForMax();
        } else {
            viewResponse("Transfer amount cannot exceed " + "₦" + currentTransferLimit);
            Log.i(TAG, "amount is too high:::::: ");
        }

    }

    void checkForMax() {
        double amt = etAmount.getNumericValue();
        ProfileParser.Amount = String.valueOf(amt);
        ProfileParser.destination = etAccountNumber.getText().toString().trim();
        ProfileParser.vtudescription = "KOLOMONI CASH TRANSFER";
        ProfileParser.totalAmount = ProfileParser.Amount;
//        ProfileParser.Fee = "0.00";
        ProcessRule(ProfileParser.ctransferrule, ProfileParser.totalAmount);
        ProcessRuleSA(ProfileParser.satfeerule, ProfileParser.totalAmount);
        ProcessRuleSS(ProfileParser.wtsupersuper, ProfileParser.totalAmount);

        String fee = printInAmountFormat(Double.valueOf(ProfileParser.Amount.isEmpty() ?
                ProfileParser.amount : ProfileParser.Amount) > 19999 ? "50" : String.valueOf(0.5 *
                Double.valueOf(ProfileParser.Amount.isEmpty() ? ProfileParser.amount : ProfileParser.Amount)));

        AlertDialog.Builder builder1 = new AlertDialog.Builder(CardTransferActivity.this);
        builder1.setCancelable(false);
        builder1.setTitle("Transaction Summary");
        builder1.setMessage("Account Number: " + ProfileParser.destination + "\n" +
                "Account Name: " + ProfileParser.vtureceivername + "\n" + "Amount: " + "NGN "+ProfileParser.Amount +
                "\n" + "Fee: " + "NGN 30.0");

        builder1.setPositiveButton(
                "Proceed",
                (dialog, id) -> {
                    dialog.cancel();

//                        Intent intent2 = new Intent();
//                        intent2.setClass(getApplicationContext(), EMV.class);
//                        intent2.putExtra("transaction", "CASH TRANSFER");
//                        startActivity(intent2);

                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    private String printInAmountFormat(String number) {
        if (number.isEmpty())
            return "NGN 0.00";
        Log.i(TAG, "printInAmountFormat");
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String fin = "";
        Log.i(TAG, "LENGTH: " + formatter.format(amount).length());
        if (formatter.format(amount).length() < 4)
            fin = ProfileParser.curabbr + " 0" + formatter.format(amount);
        else
            fin = ProfileParser.curabbr + " " + formatter.format(amount);
        return fin;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


    public ArrayList<Frequent_BankList> getFrequentBanks() {

        for (int i = 0; i < frequent_bank_typ.length; i++) {

            Frequent_BankList item;
            item = new Frequent_BankList(frequent_bank_typ[i], "https://kams.kolomoni.ng/frequentbankgetbankimage/" + frequent_bank_image[i], String.valueOf(i), frequent_bank_keys[i], frequent_bank_code[i]);
            frequent_banklist_items.add(item);
        }

        return frequent_banklist_items;
    }

    public void selectBank() {
        final Dialog dialog_data = new Dialog(CardTransferActivity.this);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog_data.getWindow().setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.custom_alertdialog);
        dialog_data.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        TextView alertdialog_textview = dialog_data.findViewById(R.id.alertdialog_textview);
        alertdialog_textview.setText("Select bank");

        ImageView dialog_cancel_btn = dialog_data.findViewById(R.id.dialog_cancel_btn);
        dialog_cancel_btn.setOnClickListener(v -> {
            if (dialog_data != null) {
                dialog_data.dismiss();
                dialog_data.cancel();
            }

        });

        EditText filterText = dialog_data.findViewById(R.id.alertdialog_edittext);
        ListView alertdialog_Listview = dialog_data.findViewById(R.id.alertdialog_Listview);
//        SearchView searchView = dialog_data.findViewById(R.id.search_box);

        adapter = new BankListAdapter(CardTransferActivity.this,
                R.layout.spinner_bank_list_custom_layout, getItems());
        alertdialog_Listview.setAdapter(adapter);
        alertdialog_Listview.setTextFilterEnabled(true);

        alertdialog_Listview.setOnItemClickListener((a, v, position, id) -> {
//                Utility.hideKeyboard(context,v);

            BankList bankList = (BankList) a.getItemAtPosition(position);

            String bankName = bankList.getBankName();

            //data.setText(String.valueOf(a.getItemAtPosition(position)));
            Log.i(TAG, "Bank name : " + bankName + " ::: BankCode: " + bankList.getBankCode());

            ProfileParser.vtubankcode = bankList.getKeys();
            ProfileParser.vtubankname = bankName;
            ProfileParser.vtubankkey = bankList.getKeys();

            spinnerSelectBank.setText(bankName);

            Log.i(TAG, "DATA vtubankcode : " + ProfileParser.vtubankcode);
            Log.i(TAG, "DATA vtubankname : " + ProfileParser.vtubankname);
            Log.i(TAG, "DATA vtubankkey : " + ProfileParser.vtubankkey);

            if (dialog_data != null) {
                dialog_data.dismiss();
                dialog_data.cancel();
            }
        });

        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
//                adapter.notifyDataSetChanged();
            }
        });

        dialog_data.show();

    }

    private ArrayList<BankList> getItems() {

        String server_response = sharedPreferences.getString(Storage.BANK_LIST, "");
//        Log.i(TAG, "server list: "+server_response);

        Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
//                Log.i(TAG, "RECIEVE: " + server_response);

        items.clear();
        JSONObject obj = null;
        try {
            String str = sharedPreferences.getString(Storage.BANK_LIST, "");
            obj = new JSONObject(str);
            JSONArray arr = obj.getJSONArray("message");

            for (int i = 0; i < arr.length(); i++) {
                String bankName = arr.getJSONObject(i).getString("typ");
                String bankLogoUrl = arr.getJSONObject(i).getString("picture_link");
                String bankId = arr.getJSONObject(i).getString("bank_id");
                String keys = arr.getJSONObject(i).getString("keys");
                String bankCode = arr.getJSONObject(i).getString("bank_code");
                BankList item;

                item = new BankList(bankName, bankLogoUrl, bankId, keys, bankCode);
                items.add(item);

//                        Log.i(TAG, "RECIEVE: " + item.getName() +" - "+ item.getImage());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;

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

    private void viewResponse(String input) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setCancelable(false);
        builder1.setMessage(input);

        builder1.setPositiveButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

//        builder1.setNegativeButton(
//                "CANCEL",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        dialog.cancel();
//                        Intent intent = new Intent();
//                        intent.setClass(Login.this, Login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

    @Override
    public void OnBankClick(int position) {
        System.out.println("this is the position of the list " + position);

        Frequent_BankList frequent_bankList = frequent_banklist_items.get(position);

        String bankName = frequent_bankList.getBankName();

        //data.setText(String.valueOf(a.getItemAtPosition(position)));
        Log.i(TAG, "Bank name : " + bankName + " ::: BankCode: " + frequent_bankList.getBankCode());

        ProfileParser.vtubankcode = frequent_bankList.getBankCode();
        ProfileParser.vtubankname = bankName;
        ProfileParser.vtubankkey = frequent_bankList.getKeys();

        spinnerSelectBank.setText(bankName);

        Log.i(TAG, "DATA vtubankcode : " + ProfileParser.vtubankcode);
        Log.i(TAG, "DATA vtubankname : " + ProfileParser.vtubankname);
        Log.i(TAG, "DATA vtubankkey : " + ProfileParser.vtubankkey);
    }

    public class GetMethodAccountName extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                if (ProfileParser.vtubankname.equals("GUARANTY TRUST BANK PLC")) {
                    urlConnection.setRequestProperty("bankcode", ProfileParser.vtubankkey);
                    urlConnection.setRequestProperty("destination", ProfileParser.destination);
                } else {
                    urlConnection.setRequestProperty("bankcode", ProfileParser.vtubankkey);
                    urlConnection.setRequestProperty("destination", ProfileParser.destination);
                }

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            barrier.setVisibility(View.GONE);
                            Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                            Log.i(TAG, "RECIEVE: " + server_response);

                            ProfileParser.txnNumber = "4";

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                JSONObject jb = obj.getJSONObject("GetAccountInOtherBankResult");
                                JSONObject rs = jb.getJSONObject("Response");
                                if (!rs.toString().contains("Invalid Amount")) {

                                    ProfileParser.vtureceivername = rs.getString("ACCOUNTNAME");
                                    cdAmount.setVisibility(View.VISIBLE);
                                    tvAmount.setVisibility(View.VISIBLE);

                                    try {
                                        tvValidatedName.setText(rs.getString("ACCOUNTNAME"));
//                                        tvValidateButton.setText("Proceed");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "INVALID ACCOUNT", Toast.LENGTH_LONG).show();
//                                    removeFragment(BankFragment.this);
//                                    ((MainActivity) getApplicationContext()).displayMainView();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "INVALID ACCOUNT", Toast.LENGTH_LONG).show();
//                                removeFragment(BankFragment.this);
//                                ((MainActivity) getApplicationContext()).displayMainView();

                            }

                        }
                    });
                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            barrier.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "CTAMS DOWN", Toast.LENGTH_LONG).show();

//                            removeFragment(BankFragment.this);
//                            ((MainActivity) getApplicationContext()).displayMainView();
                        }
                    });
                }
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        barrier.setVisibility(View.GONE);
                    }
                });

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("Response", "" + server_response);

        }
    }


}