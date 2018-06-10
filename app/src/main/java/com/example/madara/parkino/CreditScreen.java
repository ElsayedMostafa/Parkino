package com.example.madara.parkino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.craftman.cardform.CardForm;

public class CreditScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_screen);
        CardForm cardForm = (CardForm) findViewById(R.id.credit_card_form);
        TextView paymentAmount = (TextView) findViewById(R.id.payment_amount);
        Button payButton = (Button) findViewById(R.id.btn_pay);
        paymentAmount.setText("$199");
        payButton.setAllCaps(false);
        payButton.setText("Charge");
    }
}
