package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.User;
import com.example.madara.parkino.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "RegisterActivity";
    private Call<MainResponse> mRegisterCall;
    private EditText mRegisterUsername, mRegisterEmail, mRegisterPassword, mRegisterRepeatPassword, mRegisterPhone;
    private TextView mRegisterGoToLogin;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        mRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        mRegisterEmail = (EditText) findViewById(R.id.et_register_email);
        mRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        mRegisterRepeatPassword = (EditText) findViewById(R.id.et_register_re_password);
        mRegisterPhone = (EditText) findViewById(R.id.et_register_phone);
        mRegisterGoToLogin = (TextView) findViewById(R.id.tv_register_gotologin);
        mRegister = (Button) findViewById(R.id.btn_register);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        mRegister.setOnClickListener(this);
        mRegisterGoToLogin.setOnClickListener(this);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                final User user = new User();
                if (!validate()) {
                    onSignupFailed();
                    return;
                }
                mRegister.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(RegisterScreen.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating Account...");
                progressDialog.show();

                user.username = mRegisterUsername.getText().toString();
                user.email = mRegisterEmail.getText().toString();
                user.password = mRegisterPassword.getText().toString();
                user.phone_number = mRegisterPhone.getText().toString();
                mRegisterCall = WebService.getInstance().getApi().registerUser(user);
                mRegisterCall.enqueue(new Callback<MainResponse>() {
                    @Override
                    public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                        if (!mRegisterCall.isCanceled()) {
                            try {
                                if (response.body().status == 2) {
                                    Toast.makeText(RegisterScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    mRegister.setEnabled(true);
                                    progressDialog.cancel();
                                } else if (response.body().status == 1) {
                                    Toast.makeText(RegisterScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    // go to login activity
                                    Intent gotToLogin = new Intent(RegisterScreen.this, LoginScreen.class);
                                    gotToLogin.putExtra("email", user.email);
                                    gotToLogin.putExtra("pass", user.password);
                                    startActivity(gotToLogin);
                                    progressDialog.cancel();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                    mRegister.setEnabled(true);
                                }

                            } catch (Exception e) {
                                Toast.makeText(RegisterScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                                mRegister.setEnabled(true);
                                progressDialog.cancel();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainResponse> call, Throwable t) {
                        if (!mRegisterCall.isCanceled()) {
                            Log.e(TAG, t.getLocalizedMessage());
                            progressDialog.cancel();
                            Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                            mRegister.setEnabled(true);
                        }

                    }
                });
                break;
            case R.id.tv_register_gotologin:
                Intent gotToLog = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(gotToLog);
                finish();
                break;


        }

    }

    public void onSignupFailed() {
        mRegister.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mRegisterUsername.getText().toString();
        String email = mRegisterEmail.getText().toString();
        String password = mRegisterPassword.getText().toString();
        String repeat_password = mRegisterRepeatPassword.getText().toString();
        String phoneNumber = mRegisterPhone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mRegisterUsername.setError("Username should be at least 3 characters");
            valid = false;
        } else {
            mRegisterUsername.setError(null);
        }
        if (phoneNumber.isEmpty() || phoneNumber.length() != 11) {
            mRegisterPhone.setError("Invalid phone number");
            valid = false;
        } else {
            mRegisterPhone.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mRegisterEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            mRegisterEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mRegisterPassword.setError("Password between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mRegisterPassword.setError(null);
        }
        if (!password.equals(repeat_password)) {
            mRegisterRepeatPassword.setError("Passwords not identical");
            valid = false;
        } else {
            mRegisterRepeatPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRegisterCall != null) {
            mRegisterCall.cancel();
        }
    }
}
