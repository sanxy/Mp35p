package com.capitalsage.mp35p.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.capitalsage.mp35p.R;
import com.cottacush.android.currencyedittext.CurrencyEditText;

public class WithdrawalActivity extends Activity {

    private static final String TAG = WithdrawalActivity.class.getSimpleName();
    CurrencyEditText etAmount;
    String[] accountTypes = {"Choose Account", "DEFAULT ACCOUNT", "SAVINGS ACCOUNT", "CURRENT ACCOUNT", "CREDIT ACCOUNT"};
    Spinner spinnerSelectAccountType;
    private String curr;
    String sAccountType = "", sAccountTypeValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        curr = "₦";

        etAmount = findViewById(R.id.edit_text_amount);
        spinnerSelectAccountType = findViewById(R.id.spinner_select_service);

       findViewById(R.id.back_cash_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        findViewById(R.id.withdrawal_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proceedWithCashOut();

            }
        });

        etAmount.setCurrencySymbol(curr, true);

        //Creating the ArrayAdapter instance having the service list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accountTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerSelectAccountType.setAdapter(adapter);

        spinnerSelectAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sAccountType = accountTypes[position];
                if (sAccountType.equals("DEFAULT ACCOUNT")) {
                    sAccountTypeValue = "00";
                } else if (sAccountType.equals("SAVINGS ACCOUNT")) {
                    sAccountTypeValue = "10";
                } else if (sAccountType.equals("CURRENT ACCOUNT")) {
                    sAccountTypeValue = "20";
                } else if (sAccountType.equals("CREDIT ACCOUNT")) {
                    sAccountTypeValue = "30";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void proceedWithCashOut() {
        if (!sAccountType.equals("Choose Account")) {
            if (!etAmount.getText().toString().equals("")) {

                Toast.makeText(getApplicationContext(), "Input amount::: "+ etAmount.getText().toString(), Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getApplicationContext(), "Input amount to cash out", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Choose account first", Toast.LENGTH_SHORT).show();
        }
    }


}