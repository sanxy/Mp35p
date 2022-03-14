package com.capitalsage.mp35p.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.capitalsage.mp35p.R;

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
//                    Toast.makeText(requireActivity(), "Select a network", Toast.LENGTH_SHORT).show();
                } else if (sService.equals("MTN")) {
//                    ProfileParser.vtuprovider = "MTNDATA";
//                    Toast.makeText(requireActivity(), "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
//                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("AIRTEL")) {
//                    ProfileParser.vtuprovider = "AIRTELDATA";
//                    Toast.makeText(requireActivity(), "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
//                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("GLO")) {
//                    ProfileParser.vtuprovider = "GLODATA";
//                    Toast.makeText(requireActivity(), "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
//                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                } else if (sService.equals("9MOBILE")) {
//                    ProfileParser.vtuprovider = "9MOBILEDATA";
//                    Toast.makeText(requireActivity(), "FETCHING DETAILS. PLEASE WAIT", Toast.LENGTH_SHORT).show();
//                    new GetMethodDemo().execute(BASEURL + "/ctams/processor/datalookup");
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.back_data).setOnClickListener(view -> finish());

        findViewById(R.id.data_button).setOnClickListener(view -> {
            hideKeyboard();
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

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}