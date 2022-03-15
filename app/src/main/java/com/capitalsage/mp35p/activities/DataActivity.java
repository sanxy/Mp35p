package com.capitalsage.mp35p.activities;

import static com.capitalsage.mp35p.utils.ProfileParser.BASEURL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.database.DATABASEHANDLER;
import com.capitalsage.mp35p.database.HOSTKEYS;
import com.capitalsage.mp35p.utils.ProfileParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DataActivity extends Activity {

    private static final String TAG = DataActivity.class.getSimpleName();

    String[] serviceNames = {"Choose a network", "MTN", "AIRTEL", "GLO", "9MOBILE"};

    Spinner spinnerSelectService, spinnerSelectPlan;
    EditText etPhoneNumber;
    TextView selectedPlan;
    String sService = "", sPhoneNumber;

    String[] typ;
    List<String> usersList;
    ArrayAdapter<String> catAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        spinnerSelectService = findViewById(R.id.spinner_select_service);
        spinnerSelectPlan = findViewById(R.id.spinner_select_plan);

        etPhoneNumber = findViewById(R.id.edit_text_phone_number);
        selectedPlan = findViewById(R.id.selected_plan);

        usersList = new ArrayList<>();

        //Creating the ArrayAdapter instance having the service list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerSelectService.setAdapter(adapter);

        spinnerSelectService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sService = serviceNames[position];
                selectedPlan.setText("Selected plan appears here");

                //clear the spinner for select a plan
                usersList.clear();
                spinnerSelectPlan.setAdapter(null);

                if (sService.equals("Choose a network")) {
//                    Toast.makeText(DataActivity.this, "Select a network", Toast.LENGTH_SHORT).show();
                } else if (sService.equals("MTN")) {
                    ProfileParser.vtuprovider = "MTNDATA";
                    Toast.makeText(DataActivity.this, "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("AIRTEL")) {
                    ProfileParser.vtuprovider = "AIRTELDATA";
                    Toast.makeText(DataActivity.this, "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("GLO")) {
                    ProfileParser.vtuprovider = "GLODATA";
                    Toast.makeText(DataActivity.this, "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("9MOBILE")) {
                    ProfileParser.vtuprovider = "9MOBILEDATA";
                    Toast.makeText(DataActivity.this, "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.back_data).setOnClickListener(view -> finish());

        findViewById(R.id.data_button).setOnClickListener(view -> {
            hideSoftKeyboard(DataActivity.this);

            if (validate()) {
                if (selectedPlan.equals("Selected plan appears here")) {
                    Toast.makeText(getApplicationContext(), "Select a plan", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


    }

    private boolean validate() {
        boolean valid = true;

        sPhoneNumber = etPhoneNumber.getText().toString().trim();

        if (sPhoneNumber.isEmpty() || sPhoneNumber.length() < 10) {
            etPhoneNumber.setError("at least 11 characters");
            valid = false;
        } else {
            etPhoneNumber.setError(null);
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

    public class GetMethodDemo extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("tid", ProfileParser.configtid);
//                urlConnection.setRequestProperty("tid", "2082QS19");
                urlConnection.setRequestProperty("service", ProfileParser.vtuprovider);
                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "TID: " + ProfileParser.configtid);
                Log.i(TAG, "SERVICE: " + ProfileParser.vtuprovider);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                            Log.i(TAG, "RECIEVE: " + server_response);

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                JSONArray arr = obj.getJSONArray("data");
                                typ = new String[arr.length()];
                                for (int i = 0; i < arr.length(); i++) {
                                    typ[i] = arr.getJSONObject(i).getString("description");
                                    usersList.add(typ[i]);
                                }

                                showSelectedDataPlan(server_response);
                                catAdapter = new ArrayAdapter<>(DataActivity.this, android.R.layout.simple_list_item_1, usersList);
                                spinnerSelectPlan.setAdapter(catAdapter);

                                catAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DataActivity.this, "CTAMS DOWN", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
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

    private void showSelectedDataPlan(String server_response) {

        spinnerSelectPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //get the value selected/displayed in the spinner
                String value = catAdapter.getItem(position);

//                Log.i(TAG, "DATA VALUE: " + value);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(server_response);
                    JSONArray arr = obj.getJSONArray("data");

                    for (int i = 0; i < arr.length(); i++) {
                        if (arr.getJSONObject(i).getString("description").equals(value)) {
                            ProfileParser.code = arr.getJSONObject(i).getString("code");
                            ProfileParser.description = arr.getJSONObject(i).getString("description");
                            ProfileParser.amount = arr.getJSONObject(i).getString("amount");
                            ProfileParser.value = arr.getJSONObject(i).getString("amount");
                            ProfileParser.duration = arr.getJSONObject(i).getString("duration");

                            Log.i(TAG, "DATA AMOUNT: " + ProfileParser.amount);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                selectedPlan.setText(ProfileParser.vtuprovider + " " + ProfileParser.description + " " + ProfileParser.amount);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    Boolean getHostDetails(String txn) {
        String hos = "";
        Log.i(TAG, "PRESSED 2: " + txn);
        String check = txn + "###host1";
        if (ProfileParser.hostarray.toLowerCase().contains(check.toLowerCase()))
            hos = "HOSTA";
        else
            hos = "HOSTB";
        Log.i(TAG, "PRESSED 3: " + hos);
        ProfileParser.hostToUse = hos;
        DATABASEHANDLER db = new DATABASEHANDLER(DataActivity.this);
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

            if (ProfileParser.fhostencmsk == null || ProfileParser.fhostencsk == null ||
                    ProfileParser.fhostencpk == null || ProfileParser.fhostclrmsk == null ||
                    ProfileParser.fhostclrsk == null || ProfileParser.fhostclrpk == null ||
                    ProfileParser.fhostip == null || ProfileParser.fhostport == null
                    || ProfileParser.fhostssl == null || ProfileParser.fhostmnl == null
                    || ProfileParser.fhostmid == null) {
                return gotoHome();
            } else {
                return true;
            }
        }
        return false;
    }

    Boolean gotoHome() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DataActivity.this);
        builder1.setCancelable(false);
        builder1.setMessage("KEYS NOT LOADED...");

        builder1.setPositiveButton(
                "DOWNLOAD KEYS",
                (dialog, id) -> {
                    dialog.cancel();

                    Intent intent = new Intent();
                    intent.setClass(DataActivity.this, ProfileDownload.class);
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

}