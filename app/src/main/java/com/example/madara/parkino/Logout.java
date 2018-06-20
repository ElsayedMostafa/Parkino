package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.madara.parkino.utils.Session;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Session.getInstance().logoutsecurity(this);
        final ProgressDialog progressDialog = new ProgressDialog(Logout.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logout...");
        progressDialog.show();
        Session.getInstance().logoutAndGoToLogin(Logout.this);
        progressDialog.cancel();
        finish();

    }
}
