package com.capitalsage.mp35p.activities;

import static com.capitalsage.mp35p.utils.ProfileParser.APPVERSION;
import static com.capitalsage.mp35p.utils.ProfileParser.BASEURL;
import static com.capitalsage.mp35p.utils.ProfileParser.BRAND;
import static com.capitalsage.mp35p.utils.ProfileParser.MODEL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.database.DATABASEHANDLER;
import com.capitalsage.mp35p.utils.ProfileParser;
import com.capitalsage.mp35p.utils.Storage;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ProfileDownload extends Activity {

    private static final String TAG = ProfileDownload.class.getSimpleName();
    private TextView disp;
    LinearProgressIndicator progressIndicator;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_download);

        initUI();
    }

    private void initUI() {
        sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);

        disp = findViewById(R.id.text_id);
        progressIndicator = findViewById(R.id.progress_bar);

        disp.setText("Processing request");
        new GetMethodDemo().execute(BASEURL + "/ctams/profile/hackeruse/");
        //new GetMethodDemo().execute(BASEURL + "/ctams/profile/tempprofile/");
        Log.i(TAG, "Done doing Get");
//        Utilities.writeStringAsFile("0", "receipt.txt", getApplicationContext());
    }

    public class GetMethodDemo extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                //Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("brand", BRAND);
                urlConnection.setRequestProperty("model", MODEL);
                urlConnection.setRequestProperty("serial", ProfileParser.emailaddress);
                urlConnection.setRequestProperty("appversion", APPVERSION);
                urlConnection.setRequestProperty("token", ProfileParser.token);
                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "WISDOM VERSION: " + APPVERSION);
                Log.i(TAG, "WISDOM MODEL: " + MODEL);
                Log.i(TAG, "WISDOM BRAND: " + BRAND);
//                Log.i(TAG, "WISDOM PHONE SN: " + Utilities.getSerialNumber());
                Log.i(TAG, "WISDOM SERIAL: " + ProfileParser.emailaddress);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            disp.setText("Profile Loaded");
                        }
                    });
                    Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                    ProfileParser.parseNewProfile(server_response);
                    Log.i(TAG, "DONE PARSING");
                    Log.i(TAG, "ABOUT TO SAVE");
                    Log.i(TAG, "DONE SAVING");
                    Log.i(TAG, "TRANSACTION: " + ProfileParser.transactions);
                    Log.i(TAG, "HOST 1 CTMK: " + ProfileParser.swkcomp1);
                    Log.i(TAG, "HOST 1 SSL: " + ProfileParser.hostssl);
                    Log.i(TAG, "HOST 2 SSL: " + ProfileParser.host2ssl);
                    Log.i(TAG, "VERSION: " + ProfileParser.appversion);
                    Log.i(TAG, "BRAND: " + ProfileParser.appbrand);
                    Log.i(TAG, "MODEL: " + ProfileParser.appmodel);
                    Log.i(TAG, "FIX: " + ProfileParser.appfix);
                    Log.i(TAG, "ADMIN PIN: " + ProfileParser.configadminpin);
                    Log.i(TAG, "config tid::: " + ProfileParser.configtid);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "PRESSED Storing host a: " + ProfileParser.hostip);
                            Log.i(TAG, "PRESSED Storing host b: " + ProfileParser.hostport);
                            Log.i(TAG, "PRESSED Storing host c: " + ProfileParser.hostssl);
                            Log.i(TAG, "FORCEALLTERMINALS: " + ProfileParser.forceallterminals);
                            Log.i(TAG, "CURRENT VERSION: " + ProfileParser.currentversion);
                            Log.i(TAG, "APP VERSION: " + APPVERSION);
                            if (ProfileParser.forceallterminals.equals("true") && !ProfileParser.currentversion.equals(APPVERSION)) {
                                //update the app
//                                UpdateApp();
                            } else if (ProfileParser.paramdownload == null || ProfileParser.paramdownload.equals("null")) {
                                // get last 5 transaction list from kams
                                new GetMethodLastTransactions().execute("https://kams.kolomoni.ng/api/checks/getlasttransactions");

                            } else if (ProfileParser.paramdownload.length() < 120) {
                                // get last 5 transaction list from kams
                                new GetMethodLastTransactions().execute("https://kams.kolomoni.ng/api/checks/getlasttransactions");

                            } else {
                                //if(!ProfileParser.paramdownload.isEmpty()) {
                                DATABASEHANDLER db2 = new DATABASEHANDLER(ProfileDownload.this);
                                db2.deleteHost();
                                db2.HostInit("HOSTA");
                                db2.HostInit("HOSTB");

                                DATABASEHANDLER db = new DATABASEHANDLER(getApplicationContext());
                                db.HostFirstStep("HOSTA", ProfileParser.hostname, ProfileParser.hostip, ProfileParser.hostport,
                                        ProfileParser.hostssl, ProfileParser.hostfriendlyname, ProfileParser.hostremarks);
                                db.HostFirstStep("HOSTB", ProfileParser.host2name, ProfileParser.host2ip, ProfileParser.host2port,
                                        ProfileParser.host2ssl, ProfileParser.host2friendlyname, ProfileParser.host2remarks);
                                Log.i(TAG, "SUPERBILLER: " + ProfileParser.profilevas);
                                SystemClock.sleep(1000);

                                MainActivity.encmsk = ProfileParser.encmstkey;
                                MainActivity.clrmsk = ProfileParser.clrmstkey;
                                MainActivity.encsk = ProfileParser.encseskey;
                                MainActivity.clrsk = ProfileParser.clrseskey;
                                MainActivity.encpk = ProfileParser.encpinkey;
                                MainActivity.clrpk = ProfileParser.clrpinkey;
                                MainActivity.ctmk = ProfileParser.swkcomp1;

                                db.HostSecondStep("HOSTA", MainActivity.ctmk, MainActivity.encmsk, MainActivity.encsk,
                                        MainActivity.encpk, MainActivity.clrmsk, MainActivity.clrsk, MainActivity.clrpk);

                                String key = ProfileParser.paramdownload;
                                String ctmkdatetime = "";
                                String mid = "";
                                String timeout = "";
                                String currencycode = "";
                                String countrycode = "";
                                String callhome = "";
                                String mnl = "";
                                String mcc = "";

                                String f42 = key;
                                int loop = 0, i = 0;
                                String t = "";
                                String l = "";
                                String v = "";
                                for (i = 0; i < f42.length(); i++) {
                                    if (loop == 0) {
                                        t = f42.substring(i, i + 2);
                                        i = i + 1;
                                        loop++;
                                    } else if (loop == 1) {
                                        l = f42.substring(i, i + 3);
                                        i = i + 2;
                                        loop++;
                                    } else if (loop == 2) {
                                        int f = Integer.parseInt(l);
                                        v = f42.substring(i, i + f);
                                        loop = 0;
                                        i = i + f - 1;
                                        switch (t) {
                                            case "02":
                                                ctmkdatetime = v;
                                                break;
                                            case "03":
                                                mid = v;
                                                break;
                                            case "04":
                                                timeout = v;
                                                break;
                                            case "05":
                                                currencycode = v;
                                                break;
                                            case "06":
                                                countrycode = v;
                                                break;
                                            case "07":
                                                callhome = v;
                                                break;
                                            case "08":
                                                mcc = v;
                                                break;
                                            case "52":
                                                mnl = v;
                                                break;
                                            default:
                                                Log.i(TAG, "UNKNOWN TLV...");
                                                break;
                                        }


                                        Log.i(TAG, "TAG: " + t);
                                        Log.i(TAG, "LENGTH: " + l);
                                        Log.i(TAG, "VALUE: " + v);
                                    }

                                }

                                db.HostThirdStep("HOSTA", ctmkdatetime, mid, timeout, currencycode, countrycode,
                                        callhome, mnl, mcc);
                                ProfileParser.fhostmid = mid;

                                // get daily transactions
                                new GetMethodLastTransactions().execute("https://kams.kolomoni.ng/api/checks/getlasttransactions");
                            }
                        }
                    });
                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject obj = null;
                            try {
                                if (server_response == null) {
                                    assert obj != null;
                                    String detail = obj.getString("message");
                                    viewResponse(detail);

//                                    Toast.makeText(getApplicationContext(), "Error: Profile incomplete", Toast.LENGTH_LONG).show();

//                                    Intent intent = new Intent();
//                                    intent.setClass(ProfileDownload.this, Login.class);
//                                    startActivity(intent);
//                                    finish();
                                } else {
                                    obj = new JSONObject(server_response);
                                    String res = obj.getString("message");
                                    viewResponse(res);
                                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject obj = null;

                                Log.i(TAG, "server_response: " + server_response);

                                try {
                                    if (server_response == null) {
//                                        assert obj != null;
//                                        obj = new JSONObject(server_response);
//                                        String detail = obj.getString("message");
//                                        Log.e("Checking", "detail::: "+detail);
                                        viewResponse("Server Error");
//                                        Toast.makeText(getApplicationContext(), "Error: Profile incomplete", Toast.LENGTH_LONG).show();

//                                        Intent intent = new Intent();
//                                        intent.setClass(ProfileDownload.this, Login.class);
//                                        startActivity(intent);
//                                        finish();

                                    } else {
                                        obj = new JSONObject(server_response);
                                        String res = obj.getString("message");
                                        viewResponse(res);
                                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("Response", "" + server_response);

        }
    }

    public class GetMethodBankList extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String basicAuth = "Bearer " + ProfileParser.tokenKams;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", basicAuth);

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "TOKEN KAMS: " + ProfileParser.tokenKams);


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response init:: " + server_response);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                            Log.i(TAG, "RECIEVE: " + server_response);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strDate = sdf.format(c.getTime());

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Storage.BANK_LIST, server_response);
                            editor.putString(Storage.BANK_LIST_DATE, strDate);
                            editor.apply();

                            //query all bank images and add to bitmap array

                            //bank list received, save and proceed to main activity
                            Intent intent = new Intent();
                            intent.setClass(ProfileDownload.this, MainActivity.class);
                            intent.putExtra("where", "Profile Download");
                            intent.putExtra("when", "After Successful Profile Download");
                            startActivity(intent);
                            finish();

                        }
                    });
                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE:: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Kams DOWN", Toast.LENGTH_LONG).show();
//                            removeFragment(DataFragment.this);

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

            Log.i(TAG, "Response:: " + server_response);

        }
    }

    public class GetMethodLastTransactions extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String basicAuth = "Bearer " + ProfileParser.tokenKams;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", basicAuth);

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "TOKEN KAMS: " + ProfileParser.tokenKams);


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response: " + server_response);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LENGTH OF RECIEVE: " + server_response.length());
                            Log.i(TAG, "RECIEVE Transactions: " + server_response);

                            //save last transaction query
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Storage.LAST_FIVE_TRANSACTION, server_response);
                            editor.apply();

                            disp.setText("Loading user profile");
                            progressIndicator.setIndicatorColor(getResources().getColor(R.color.green));
                            progressIndicator.setProgressCompat((int) 80, true);
                            fetchUserDetails();

                        }
                    });
                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Kams DOWN", Toast.LENGTH_LONG).show();
//                            removeFragment(DataFragment.this);

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

    private void fetchUserDetails() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                // You can perform your task here.
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, "");

                OkHttpClient client = new OkHttpClient();
                String basicAuth = "Bearer " + ProfileParser.tokenKams;

                Request request = new Request.Builder()
                        .url("https://kams.kolomoni.ng/api/checks/user-details")
                        .post(body)
                        .addHeader("Authorization", basicAuth) //Notice this request has header if you don't need to send a header just erase this part
                        .build();

                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();

                        Log.i(TAG, "onResponse(): " + response);
                        Log.i(TAG, "onResponse user details: " + result);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(result);

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Storage.FULL_NAME, obj.getJSONObject("message").getString("fullname"));
                            editor.putString(Storage.BVN, obj.getJSONObject("message").getString("bvn"));
                            editor.putString(Storage.ADDRESS, obj.getJSONObject("message").getString("address"));
                            editor.putString(Storage.STATE, obj.getJSONObject("message").getString("state"));
                            editor.putString(Storage.LGA, obj.getJSONObject("message").getString("lga"));
                            editor.putString(Storage.DOB, obj.getJSONObject("message").getString("dob"));
                            editor.putString(Storage.MOTHER_MAIDEN_NAME, obj.getJSONObject("message").getString("mother_maiden_name"));
                            editor.putString(Storage.PHONE, obj.getJSONObject("message").getString("phonenumber"));
                            editor.putString(Storage.KYC_LEVEL, obj.getJSONObject("message").getString("kyclevel"));
                            editor.putString(Storage.KYC_LIMIT, obj.getJSONObject("message").getString("kyclimit"));
                            editor.putString(Storage.KYC_MAX_DAILY, obj.getJSONObject("message").getString("kycmaxdaily"));
                            editor.putString(Storage.PHONE_VERIFIED, obj.getJSONObject("message").getString("phone_verified_at"));
                            editor.putString(Storage.EMAIL_VERIFIED, obj.getJSONObject("message").getString("email_verified_at"));
                            editor.putString(Storage.PROFILE_IMAGE_LINK, obj.getJSONObject("message").getString("imagelink"));
                            editor.putString(Storage.NIN_NUMBER, obj.getJSONObject("message").getString("nin"));
                            editor.putString(Storage.NIN_VERIFIED, obj.getJSONObject("message").getString("nin_verified_at"));
                            editor.putString(Storage.TRANSFER_PIN, obj.getJSONObject("message").getString("transferpin"));
                            editor.putString(Storage.CINTRUST_ACCOUNT, obj.getJSONObject("message").getString("cintrust_account"));
                            editor.apply();

                            Handler h = new Handler(Looper.getMainLooper());
                            h.post(new Runnable() {
                                public void run() {
                                    //here show dialog
                                    new GetMethodKycLevel().execute("https://kams.kolomoni.ng/api/kyc/kyc-settings");
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG, "onFailure() Request was: " + request);

                        e.printStackTrace();
                    }

                });
            }
        });

    }

    public class GetMethodKycLevel extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String basicAuth = "Bearer " + ProfileParser.tokenKams;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", basicAuth);

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "TOKEN KAMS: " + ProfileParser.tokenKams);


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response Kyc levels: " + server_response);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            //here show dialog
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Storage.KYC_ALL_LEVEL, server_response);
                            editor.apply();
//                            ProfileParser.KycAllLevel = server_response;
                            String str = sharedPreferences.getString(Storage.FULL_NAME, "");
                            String[] splitStr = str.trim().split("\\s+");

                            disp.setText("Welcome back "+splitStr[0].substring(0,1).toUpperCase() +splitStr[0].substring(1).toLowerCase());
                            progressIndicator.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
                            progressIndicator.setProgressCompat((int) 100, true);

                            new GetMethodLastSummary().execute("https://kams.kolomoni.ng/api/checks/transaction-summary");
                        }

                    });

                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Kams DOWN", Toast.LENGTH_LONG).show();
//                            removeFragment(DataFragment.this);

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


    public class GetMethodLastSummary extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String basicAuth = "Bearer " + ProfileParser.tokenKams;
            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", basicAuth);

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "TOKEN KAMS: " + ProfileParser.tokenKams);


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "Length: " + server_response.length());
                    Log.i(TAG, "Response last summary: " + server_response);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            //here show dialog
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(Storage.LAST_TRANSACTION_SUMMARY, server_response);
                            editor.apply();

                            //get stored date and today's date and compare dates
                            String storedDate = sharedPreferences.getString(Storage.BANK_LIST_DATE, "");
                            Log.i(TAG, "storedDate date:: " + storedDate);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String todayDate = sdf.format(c.getTime());

                            try {

                                if (sharedPreferences.getString(Storage.BANK_LIST, "").equals("")||
                                        sharedPreferences.getString(Storage.BANK_LIST_DATE, "").equals("")){
                                    new GetMethodBankList().execute("https://kams.kolomoni.ng/api/checks/banklist");

                                }

                                Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(storedDate);
                                Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(todayDate);

                                Log.i(TAG, "start date:: " + start);
                                Log.i(TAG, "end date:: " + end);
                                Log.i(TAG, "daysBetween:: " + daysBetween(start, end));


                                if (daysBetween(start, end) >= 7 || storedDate.equals("")) {
                                    Log.i(TAG, "No saved date or date saved is more than 7days:: ");

                                    new GetMethodBankList().execute("https://kams.kolomoni.ng/api/checks/banklist");
                                } else {
                                    Log.i(TAG, "Date saved and bank list saved:: ");

                                    Intent intent = new Intent();
                                    intent.setClass(ProfileDownload.this, MainActivity.class);
                                    intent.putExtra("where", "Profile Download");
                                    intent.putExtra("when", "After Successful Profile Download");
                                    startActivity(intent);
                                    finish();
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

//                            new GetMethodBankList().execute("https://kams.kolomoni.ng/api/checks/banklist");
                        }

                    });

                } else {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.i(TAG, "RESPONSE: " + server_response);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Kams DOWN", Toast.LENGTH_LONG).show();
//                            removeFragment(DataFragment.this);

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

    /**
     * This method also assumes endDate >= startDate
     **/
    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    private void viewResponse(String input) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(false);
        builder1.setMessage(input);

        builder1.setPositiveButton(
                "RETRY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(ProfileDownload.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
//                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(ProfileDownload.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
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