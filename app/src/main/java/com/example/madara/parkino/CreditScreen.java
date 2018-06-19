package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.example.madara.parkino.models.ChargeRequest;
import com.example.madara.parkino.models.LoginResponse;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditScreen extends AppCompatActivity {
    private static final String TAG = "CreditScreen";
    String amount;
    private Call<MainResponse> mChagerCall;
    private int userId ;
    private int amountId;
    private int creditNumber=20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_screen);
        userId = Session.getInstance().getUser().id;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            amount = getIntent().getStringExtra("card_id");
            if(amount.equals("10")){
                amountId = 1010;
            }
            else if(amount.equals("25")){
                amountId = 2525;
            }
            else if(amount.equals("50")){
                amountId = 5050;
            }
            else if(amount.equals("100")){
                amountId = 100100;
            }

        }
        CardForm cardForm = (CardForm) findViewById(R.id.credit_card_form);
        TextView paymentAmount = (TextView) findViewById(R.id.payment_amount);
        Button payButton = (Button) findViewById(R.id.btn_pay);
        paymentAmount.setText("$"+amount);
        payButton.setAllCaps(false);
        payButton.setText("Charge");
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charge();
            }
        });
    }
    private void charge(){
        final ProgressDialog progressDialog = new ProgressDialog(CreditScreen.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Charging...");
        progressDialog.show();
        ChargeRequest chargeRequest = new ChargeRequest();
        chargeRequest.id = userId;
        chargeRequest.amountId = amountId;
        chargeRequest.creditNumber = creditNumber;
        mChagerCall = WebService.getInstance().getApi().charge(chargeRequest);
        mChagerCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if(!mChagerCall.isCanceled()){
                try{
                    if (response.body().status == 0) {
                        Toast.makeText(CreditScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        mChagerCall = null;
                    }
                    else if (response.body().status == 1) {
                        Toast.makeText(CreditScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        mChagerCall = null;
                    }
                }
                catch (Exception e){
                    Toast.makeText(CreditScreen.this,"Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    mChagerCall = null;
                }
            }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                if(!mChagerCall.isCanceled()){
                    Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    mChagerCall = null;
                }

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mChagerCall!=null){
            mChagerCall.cancel();
        }
    }
}
