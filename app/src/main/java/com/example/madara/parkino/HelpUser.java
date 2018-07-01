package com.example.madara.parkino;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by madara on 6/26/18.
 */

public class HelpUser extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent()!=null){
            String check = getIntent().getStringExtra("check");
            switch (check){
                case "1":
                    setContentView(R.layout.password_issue);
                    break;
                case "2":
                    setContentView(R.layout.rfid_issue);
                    break;
                case "3":
                    setContentView(R.layout.accuracy_issue);
                    break;
                case "4":
                    setContentView(R.layout.charge_issue);
                    break;
            }
            getSupportActionBar().setTitle(getIntent().getStringExtra("actionbar"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
