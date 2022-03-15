package com.capitalsage.mp35p.activities;


import static com.capitalsage.mp35p.utils.ProfileParser.BASEURL;

import androidx.cardview.widget.CardView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.utils.ProfileParser;
import com.capitalsage.mp35p.utils.Storage;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextInputLayout email;
    private TextInputLayout password;
    private CardView login;
    String sEmail = "", sPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Login");

                if (!validate()) {
                    onLoginFailed();
                    return;
                }

                processLogin();

            }
        });

    }

    private void initUI() {
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        login = findViewById(R.id.card_view_login);

    }

    private void processLogin() {
        JSONObject send = new JSONObject();
        try {
            send.put("username", ProfileParser.emailaddress);
            send.put("password", ProfileParser.password);
            send.put("usertype", "agent");
//            send.put("phoneserialnumber", utils.getSerialNumber());
//            send.put("count", Integer.valueOf(ProfileParser.count));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "REQUEST: " + send.toString());
        new PostCallhome().execute(BASEURL + "/ctams/login/mobileverify/", send.toString());
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        ProfileParser.emailaddress = email.getEditText().getText().toString().trim();
        ProfileParser.password = password.getEditText().getText().toString().trim();

        if (ProfileParser.emailaddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(ProfileParser.emailaddress).matches()) {
            email.setError("Enter a Valid Email");
            valid = false;
        } else {
            email.setError(null);
        }

        if (ProfileParser.password.isEmpty()) {
            password.setError("Password is Empty");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    public class PostCallhome extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                byte[] bytes = strings[1].getBytes(StandardCharsets.UTF_8);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches(false);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(bytes);
                // what should I write here to output stream to post params to server ?
                outputStream.flush();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "RESPONSE CODE: " + responseCode);
                // get response
                InputStream responseStream = null;
                if (responseCode == HttpURLConnection.HTTP_OK)
                    responseStream = new BufferedInputStream(urlConnection.getInputStream());
                else
                    responseStream = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                Log.i(TAG, "RESPONSE: " + response);
                server_response = response;
                //int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN SUCCESSFUL");
                            Log.i(TAG, "LOGIN 2: " + server_response);

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                ProfileParser.phoneserialnumber = obj.getString("phoneserialnumber");
                                ProfileParser.mposserialnumber = obj.getString("mposserialnumber");
                                ProfileParser.transferpin = obj.getString("transferpin");
                                ProfileParser.token = obj.getString("token");

                                SharedPreferences sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(Storage.EMAIL, ProfileParser.emailaddress);
                                editor.putString(Storage.PASSWORD, ProfileParser.password);

                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            loginKams();
                        }
                    });
                }
                else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN NOT SUCCESSFUL");
//                            pro.setText("LOGIN");
//                            pro.setEnabled(true);
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);
                                String res = obj.getString("message");
//                                viewResponse(res);
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
//                pro.setText("Login");
//                pro.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
//                pro.setText("Login");
//                pro.setEnabled(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
        }
    }

    public void loginKams() {
        JSONObject send = new JSONObject();
        try {
            send.put("username", ProfileParser.emailaddress);
            send.put("password", ProfileParser.password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "REQUEST KAMS: " + send.toString());
        new PostCallKams().execute("https://kams.kolomoni.ng/api/login", send.toString());

    }

    public class PostCallKams extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                Log.i(TAG, "URL: " + strings[0]);
                url = new URL(strings[0]);
                byte[] bytes = strings[1].getBytes(StandardCharsets.UTF_8);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setUseCaches(false);

                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.write(bytes);
                // what should I write here to output stream to post params to server ?
                outputStream.flush();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                Log.i(TAG, "RESPONSE CODE: " + responseCode);
                // get response
                InputStream responseStream = null;
                if (responseCode == HttpURLConnection.HTTP_OK)
                    responseStream = new BufferedInputStream(urlConnection.getInputStream());
                else
                    responseStream = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                Log.i(TAG, "RESPONSE: " + response);
                server_response = response;
                //int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN TO KAMS SUCCESSFUL");
                            Log.i(TAG, "LOGIN KAMS: " + server_response);

                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(server_response);

                                Boolean success = obj.getBoolean("success");
                                if (success){
                                    ProfileParser.tokenKams = obj.getString("access_token");

                                    /* save user details to shared preference */
                                    SharedPreferences sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
                                    sEmail = sharedPreferences.getString(Storage.EMAIL, "");
                                    sPassword = sharedPreferences.getString(Storage.PASSWORD, "");

                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, ProfileDownload.class);
                                    startActivity(intent);
                                    finish();
                                }else if (!success){
                                    String detail = obj.getString("message");
                                    viewResponse(detail);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                } else {
                    Log.i(TAG, "NOT SUCCESSFUL: " + urlConnection.getResponseMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "LOGIN NOT SUCCESSFUL TO KAMS");
                            Log.i(TAG, "KAMS: " + response);

//                            pro.setText("LOGIN");
//                            pro.setEnabled(true);

                            viewResponse("Kindly login to KAMS to verify your email");

//                            Toast.makeText(getApplicationContext(), "LOGIN NOT SUCCESSFUL TO KAMS", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "" + server_response);
        }
    }

    private void viewResponse(String input) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(false);
        builder1.setMessage(input);

        builder1.setPositiveButton(
                "LOGIN FAILED",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}