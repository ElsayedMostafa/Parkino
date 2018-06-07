package com.example.madara.parkino;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.User;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSettings extends AppCompatActivity {
    private final String TAG = "EditSetting";
    private EditText settingFirst, settingSecond, settingLast;
    private Button buttonCancel, buttonOk;
    private String actionValue, value;
    private int check, userId;
    private Call<MainResponse> mChangeInfoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);
        userId = Session.getInstance().getUser().id;
        buttonCancel = (Button) findViewById(R.id.btn_editsetting_cancel);
        buttonOk = (Button) findViewById(R.id.btn_editsetting_ok);
        settingFirst = (EditText) findViewById(R.id.et_setting_first);
        settingSecond = (EditText) findViewById(R.id.et_setting_second);
        settingLast = (EditText) findViewById(R.id.et_setting_last);
        if (getIntent() != null) {
            actionValue = getIntent().getStringExtra("action_value");
            check = getIntent().getIntExtra("check", 0);
            value = getIntent().getStringExtra("Value");
            getSupportActionBar().setTitle(actionValue);
            settingFirst.append(value);
            if (check == 2) {
                //android:inputType="textPassword"
                settingFirst.setTransformationMethod(PasswordTransformationMethod.getInstance());
                settingSecond.setTransformationMethod(PasswordTransformationMethod.getInstance());
                settingLast.setTransformationMethod(PasswordTransformationMethod.getInstance());
                settingFirst.setHint("old password");
                settingSecond.setHint("new password");
                settingLast.setHint("repeat password");
                settingSecond.setVisibility(View.VISIBLE);
                settingLast.setVisibility(View.VISIBLE);
            } else if (check == 1) {
                settingSecond.setVisibility(View.VISIBLE);
                settingSecond.setTransformationMethod(PasswordTransformationMethod.getInstance());
                settingSecond.setHint("Enter your password");
            }
        }
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment())
                //       .commit();
                onBackPressed();
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0)
                    change_name();
                else if (check == 1)
                    change_email();
                else if (check == 2)
                    change_password();
                else if (check == 3)
                    change_phone();
            }
        });
    }

    private void change_name() {
        String newName = settingFirst.getText().toString();
        if (newName.isEmpty() || newName.length() < 3) {
            settingFirst.setError("Username should be at least 3 characters");
            return;
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(EditSettings.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Name...");
            progressDialog.show();
            final User user = new User();
            user.id = userId;
            user.username = newName;
            mChangeInfoCall = WebService.getInstance().getApi().changeName(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (!mChangeInfoCall.isCanceled()) {
                        try {
                            if (response.body().status == 0) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else if (response.body().status == 1) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditSettings.this, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    if (!mChangeInfoCall.isCanceled()) {
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void change_email() {
        String newEmail = settingFirst.getText().toString();
        String password = settingSecond.getText().toString();
        if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            settingFirst.setError("Enter a valid email address");
            return;
        } else if (password.isEmpty()) {
            settingSecond.setError("Enter your password");
            return;
        } else {
            final User user = new User();
            user.id = userId;
            user.email = newEmail;
            user.password = password;
            final ProgressDialog progressDialog = new ProgressDialog(EditSettings.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Email...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changeEmail(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (!mChangeInfoCall.isCanceled()) {
                        try {
                            if (response.body().status == 0) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else if (response.body().status == 1) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditSettings.this, Logout.class));
                                progressDialog.cancel();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditSettings.this, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    if (!mChangeInfoCall.isCanceled()) {
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void change_password() {
        String oldPassword = settingFirst.getText().toString();
        String newPassword = settingSecond.getText().toString();
        String repeatPassword = settingLast.getText().toString();
        if (oldPassword.isEmpty()) {
            settingFirst.setError("Enter old password");
            return;
        } else if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            settingSecond.setError("Password between 4 and 10 alphanumeric characters");
            return;
        } else if (!newPassword.equals(repeatPassword)) {
            settingLast.setError("Passwords not identical");
            return;
        } else {
            final User user = new User();
            user.id = userId;
            user.password = newPassword;
            user.oldpassword = oldPassword;
            final ProgressDialog progressDialog = new ProgressDialog(EditSettings.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Password...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changePassword(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (!mChangeInfoCall.isCanceled()) {
                        try {
                            if (response.body().status == 0) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else if (response.body().status == 1) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                                startActivity(new Intent(EditSettings.this, Logout.class));
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditSettings.this, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    if (!mChangeInfoCall.isCanceled()) {
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void change_phone() {
        String phone = settingFirst.getText().toString();
        if (phone.isEmpty() || phone.length() != 11) {
            settingFirst.setError("Invalid phone number");
            return;
        } else {
            final User user = new User();
            user.id = userId;
            user.phone_number = phone;
            final ProgressDialog progressDialog = new ProgressDialog(EditSettings.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Change Phone number...");
            progressDialog.show();
            mChangeInfoCall = WebService.getInstance().getApi().changePhone(user);
            mChangeInfoCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (!mChangeInfoCall.isCanceled()) {
                        try {
                            if (response.body().status == 0) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            } else if (response.body().status == 1) {
                                Toast.makeText(EditSettings.this, response.body().message, Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        } catch (Exception e) {
                            Toast.makeText(EditSettings.this, "Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    if (!mChangeInfoCall.isCanceled()) {
                        progressDialog.cancel();
                        Toast.makeText(getBaseContext(), "Check network connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChangeInfoCall != null) {
            mChangeInfoCall.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
