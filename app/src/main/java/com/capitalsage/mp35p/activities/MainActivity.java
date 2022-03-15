package com.capitalsage.mp35p.activities;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.adapter.DashboardMenuAdapter;
import com.capitalsage.mp35p.model.DashboardMenu;
import com.capitalsage.mp35p.utils.ProfileParser;
import com.capitalsage.mp35p.utils.Storage;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList<DashboardMenu> dashboardMenus;
    RecyclerView recyclerViewMenu;
    public static String ctmk;
    public static String encmsk;
    public static String encsk;
    public static String encpk;
    public static String clrmsk;
    public static String clrsk;
    public static String clrpk;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
//        bankList = sharedPreferences.getString(Storage.BANK_LIST, "");

//        Log.i(TAG, "bankList::: " + bankList);
        Log.i(TAG, "config tid::: " + ProfileParser.configtid);

        recyclerViewMenu = findViewById(R.id.recycler_view_menu);

        dashboardMenus = new ArrayList<>();
        // menu view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewMenu.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        loadDashboardMenu();
    }

    // load dashboard menus
    private void loadDashboardMenu() {
        ArrayList<String> scripts = new ArrayList<String>();

        scripts.add(getString(R.string.withdrawal));
        scripts.add(getString(R.string.card_transfer));
        scripts.add(getString(R.string.airtime));
        scripts.add(getString(R.string.data));
        scripts.add(getString(R.string.pay_bills));
        scripts.add(getString(R.string.history));
        scripts.add(getString(R.string.check_account_balance));
        scripts.add(getString(R.string.disputes));
//        scripts.add(getString(R.string.services));

        for (String t : scripts) {
            DashboardMenu dashboardMenu = new DashboardMenu();
            dashboardMenu.setText(t);

            dashboardMenus.add(dashboardMenu);

            Log.i(TAG, "DashboardMenu:: " + t);
        }

        Log.i(TAG, "DashboardMenu new scripts:::: " + scripts);


        if (dashboardMenus.size() == 0) {
            recyclerViewMenu.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "DashboardMenu:: adapter is not zero");

            DashboardMenuAdapter dashboardMenuAdapter = new DashboardMenuAdapter(MainActivity.this, dashboardMenus);
            recyclerViewMenu.setAdapter(dashboardMenuAdapter);// set the Adapter to RecyclerView
        }
    }

    // get menu text and perform click on individual
    public void onClickCalled(String text) {
        // Call another activity here and pass some arguments to it.
        Intent intent = new Intent();
        Log.i(TAG, text);

        switch (text) {
            case "Withdrawal":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, WithdrawalActivity.class);
                startActivity(intent);
                break;
            case "Transfer":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, CardTransferActivity.class);
                startActivity(intent);
                break;

            case "Airtime":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, AirtimeActivity.class);
                startActivity(intent);
                break;

            case "Data":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, DataActivity.class);
                startActivity(intent);
                break;

            case "Paybill":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, PayBillsActivity.class);
                startActivity(intent);
                break;

            case "History":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
//            case "Wallet":
//                intent.putExtra("transaction", text);
//                intent.setClass(MainActivity.this, WalletActivity.class);
//                startActivity(intent);
//                break;
            case "Disputes":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, DisputesActivity.class);
                startActivity(intent);
                break;
            case "Balance":
                intent.putExtra("transaction", text);
                intent.setClass(MainActivity.this, BalanceActivity.class);
                startActivity(intent);
                break;


            default:
                Toast.makeText(getApplicationContext(), "TRANSACTION TYPE UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
        }

    }


}