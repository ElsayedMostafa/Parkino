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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.parkino.models.LoginResponse;
import com.example.madara.parkino.models.User;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LoginScreen";
    private EditText mLoginEmail, mLoginPassword;
    RelativeLayout rellay1, rellay2;
    private Button mLogin, mLoginNoAccount,mLoginForgetPassword;
    private Call<LoginResponse> mLoginCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        mLoginEmail = (EditText) findViewById(R.id.et_login_email);
        mLoginPassword = (EditText) findViewById(R.id.et_login_password);
        mLoginForgetPassword = (Button) findViewById(R.id.tv_login_forgetpassword);
        mLoginNoAccount = (Button) findViewById(R.id.tv_login_dont_have_account);
        mLogin = (Button) findViewById(R.id.btn_login);
        final boolean i =  Session.getInstance().isUserLoggedIn();
        //++++++++++++++++++++++++++++++++++++++++++
        rellay1.setVisibility(View.VISIBLE);
        rellay2.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            String email = getIntent().getStringExtra("email");
            String pass = getIntent().getStringExtra("pass");
            mLoginEmail.setText(email);
            mLoginPassword.setText(pass);
        }
        mLogin.setOnClickListener(this);
        mLoginNoAccount.setOnClickListener(this);
        //hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



    }

    private void onLoginFailed() {
        mLogin.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = mLoginEmail.getText().toString().trim();
        String password = mLoginPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mLoginEmail.setError("Enter a valid email address");
            valid = false;
        } else {
            mLoginEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mLoginPassword.setError("Password between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mLoginPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginCall != null) {
            mLoginCall.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_dont_have_account:
                Intent gotToReg = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(gotToReg);
                break;
            case R.id.btn_login:
                final User user = new User();
                if (!validate()) {
                    onLoginFailed();
                    return;
                }
                mLogin.setEnabled(false);
                final ProgressDialog progressDialog = new ProgressDialog(LoginScreen.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                user.email = mLoginEmail.getText().toString();
                user.password = mLoginPassword.getText().toString();

                mLoginCall = WebService.getInstance().getApi().loginUser(user);
                mLoginCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        // check for status value comming from server (response of login-user.php file status)
                        if (!mLoginCall.isCanceled()) {
                            try {
                                if (response.body().status == 0) {
                                    Toast.makeText(LoginScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    mLogin.setEnabled(true);
                                    progressDialog.cancel();
                                } else if (response.body().status == 1) {
                                    Toast.makeText(LoginScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    user.username = response.body().user.user_name;
                                    user.id = response.body().user.id;
                                    user.email = response.body().user.user_email;
                                    user.phone_number = response.body().user.phone_number;
                                    Session.getInstance().startSession(user);
                                    progressDialog.cancel();
                                    Intent goToMain = new Intent(LoginScreen.this, HomeScreen.class);
                                    goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    goToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(goToMain);
                                    finish();


                                } else {
                                    progressDialog.cancel();
                                    mLogin.setEnabled(true);
                                    Toast.makeText(LoginScreen.this, response.body().message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(LoginScreen.this, "Failed" + e.toString(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, e.toString());
                                mLogin.setEnabled(true);
                                progressDialog.cancel();
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        if (!mLoginCall.isCanceled()) {
                            progressDialog.cancel();
                            Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                            mLogin.setEnabled(true);
                        }

                    }
                });
                break;

        }
    }
}
