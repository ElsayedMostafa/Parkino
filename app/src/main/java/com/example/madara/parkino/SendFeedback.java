package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.madara.parkino.models.ChargeRequest;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.SendFeedbackRequest;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendFeedback extends AppCompatActivity {
    private EditText userName, userMessage;
    Button buttonSendFeedback;
    private String name, message;
    private int userId;
    private Call<MainResponse> mSendFeedbackCall;
    private ImageButton screenShot1, screenShot2, screenShot3;
    private static final int IMA_REQUEST = 400;
    private Bitmap bitmap;
    private int check = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = (EditText) findViewById(R.id.sendfeedback_user_name);
        userMessage = (EditText) findViewById(R.id.sendfeedback_user_message);
        buttonSendFeedback = (Button) findViewById(R.id.sendfeedback_btn);
        screenShot1 = findViewById(R.id.screenshot1);
        screenShot2 = findViewById(R.id.screenshot2);
        screenShot3 = findViewById(R.id.screenshot3);
        userId = Session.getInstance().getUser().id;
        buttonSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString();
                message = userMessage.getText().toString();
                if (name.trim().isEmpty()) {
                    name = Session.getInstance().getUser().username;
                }
                if (validate()) {
                    sendFeedback();
                }
            }
        });
        screenShot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                selectImage();
            }
        });
        screenShot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 2;
                selectImage();
            }
        });
        screenShot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 3;
                selectImage();
            }
        });
    }

    private void sendFeedback() {
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
                if (!mSendFeedbackCall.isCanceled()) {
                    try {
                        if (response.body().status == 0) {
                            Toast.makeText(SendFeedback.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            mSendFeedbackCall = null;
                        } else if (response.body().status == 1) {
                            Toast.makeText(SendFeedback.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            mSendFeedbackCall = null;
                        }
                    } catch (Exception e) {
                        Toast.makeText(SendFeedback.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        mSendFeedbackCall = null;
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                if (!mSendFeedbackCall.isCanceled()) {
                    Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                    mSendFeedbackCall = null;
                }

            }
        });
    }

    private boolean validate() {
        if (message.isEmpty() || message.length() < 10 || message.length() > 150) {
            userMessage.setError("Message should be between 10 and 150 characters ");
            return false;
        }
        return true;

    }

    private void selectImage() {
        Intent myImage = new Intent();
        myImage.setType("image/*");
        myImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(myImage, IMA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMA_REQUEST && data != null) {
            Uri path = data.getData();
            Log.e("TAG", String.valueOf(IMA_REQUEST));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                switch (check){
                    case 1 :
                        screenShot1.setBackgroundDrawable(bitmapDrawable);
                        break;
                    case 2 :
                        screenShot2.setBackgroundDrawable(bitmapDrawable);
                        break;
                    case 3 :
                        screenShot3.setBackgroundDrawable(bitmapDrawable);
                        break;
                }


                //screenShot1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSendFeedbackCall != null) {
            mSendFeedbackCall.cancel();
        }
    }
}
