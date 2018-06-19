package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madara.parkino.models.ChargeRequest;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.SendFeedbackRequest;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendFeedback extends AppCompatActivity {
    private EditText userName, userMessage;
    Button buttonSendFeedback;
    private String name, message;
    private int userId;
    private Call<MainResponse> mSendFeedbackCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = (EditText) findViewById(R.id.sendfeedback_user_name);
        userMessage = (EditText) findViewById(R.id.sendfeedback_user_message);
        buttonSendFeedback = (Button) findViewById(R.id.sendfeedback_btn);
        userId = Session.getInstance().getUser().id;
        buttonSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString();
                message = userMessage.getText().toString();
                if(name.trim().isEmpty()){
                    name = Session.getInstance().getUser().username;
                }
                if(validate()){
                    sendFeedback();
                }
            }
        });

    }
    private void sendFeedback(){
        final ProgressDialog progressDialog = new ProgressDialog(SendFeedback.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        SendFeedbackRequest sendFeedbackRequest = new SendFeedbackRequest();
        sendFeedbackRequest.id = userId;
        sendFeedbackRequest.userName = name;
        sendFeedbackRequest.userMessage = message;
        mSendFeedbackCall = WebService.getInstance().getApi().feedback(sendFeedbackRequest);
        mSendFeedbackCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if(!mSendFeedbackCall.isCanceled()){
                    try{
                        if (response.body().status == 0) {
                            Toast.makeText(SendFeedback.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            mSendFeedbackCall = null;
                        }
                        else if (response.body().status == 1) {
                            Toast.makeText(SendFeedback.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            mSendFeedbackCall = null;
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(SendFeedback.this,"Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        mSendFeedbackCall = null;
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                if(!mSendFeedbackCall.isCanceled()){
                    Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    mSendFeedbackCall = null;
                }

            }
        });
    }
    private boolean validate(){
        if(message.isEmpty() || message.length()<10 ||message.length()>150){
            userMessage.setError("Message should be between 10 and 150 characters ");
            return false;
        }
        return true;

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSendFeedbackCall!=null){
            mSendFeedbackCall.cancel();
        }
    }
}
