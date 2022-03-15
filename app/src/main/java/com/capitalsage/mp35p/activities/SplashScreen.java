package com.capitalsage.mp35p.activities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.capitalsage.mp35p.R;
import com.capitalsage.mp35p.utils.Storage;


public class SplashScreen extends Activity {

    String sEmail = "", sPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        /* check if user is logged in before */
        SharedPreferences sharedPreferences = getSharedPreferences(Storage.SHARED_PREFS, MODE_PRIVATE);
        sEmail = sharedPreferences.getString(Storage.EMAIL, "");
        sPassword = sharedPreferences.getString(Storage.PASSWORD, "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                This method will be executed once the timer is over
                 Start your app main activity
                */

                Intent i;
                if (sEmail.equals("") && sPassword.equals("")){
                    i = new Intent(SplashScreen.this, LoginActivity.class);

                }else{
                    i = new Intent(SplashScreen.this, MainActivity.class);
                }
                startActivity(i);
                /* close this activity */
                finish();

            }
        }, 5000);


    }

}
