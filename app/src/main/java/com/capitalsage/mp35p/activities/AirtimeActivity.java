package com.capitalsage.mp35p.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.capitalsage.mp35p.R;
import com.cottacush.android.currencyedittext.CurrencyEditText;

public class AirtimeActivity extends Activity {

    private static final String TAG = AirtimeActivity.class.getSimpleName();

    String[] serviceNames = {"Choose a network", "MTN", "AIRTEL", "GLO", "9MOBILE"};

    Spinner spinnerSelectService;
    EditText etPhoneNumber;
    String sService = "", sPhoneNumber, sAmount;
    private final int mTime = 30;
    private String curr;
    private static final int MSG_TIME_UPDATE = 100;
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
//                    ProfileParser.vtuprovider = "MTNVTU";
                }else if (sService.equals("AIRTEL")){
//                    ProfileParser.vtuprovider = "AIRTELVTU";
                }else if (sService.equals("GLO")){
//                    ProfileParser.vtuprovider = "GLOVTU";
                }else if (sService.equals("9MOBILE")){
//                    ProfileParser.vtuprovider = "9MOBILEVTU";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etAmount.setCurrencySymbol(curr, true);

        findViewById(R.id.back_airtime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    if (sService.equals("Choose a network")) {
                        Toast.makeText(getApplicationContext(), "Kindly choose a network", Toast.LENGTH_SHORT).show();
                        return;
                    }



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
}