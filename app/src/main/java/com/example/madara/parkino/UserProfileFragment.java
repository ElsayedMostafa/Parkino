package com.example.madara.parkino;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.parkino.models.UserProfileResponse;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import java.io.BufferedWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    private final String TAG = "UserProfile";
    private Call<UserProfileResponse> getProfileCall;
    private TextView userName, userPoints, userCards, userGarages, userEmail, userPhone;
    private Button buttonSettings;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = (TextView) view.findViewById(R.id.txt_profile_username);
        userPoints = (TextView) view.findViewById(R.id.txt_profile_points);
        userCards = (TextView) view.findViewById(R.id.txt_profile_cards);
        userGarages = (TextView) view.findViewById(R.id.txt_profile_garages);
        userEmail = (TextView) view.findViewById(R.id.txt_profile_useremail);
        userPhone = (TextView) view.findViewById(R.id.txt_profile_userphone);
        buttonSettings = (Button) view.findViewById(R.id.btn_profile_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        if(savedInstanceState==null){
        getUserProfile();
        }

    }

    private void getUserProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        getProfileCall = WebService.getInstance().getApi().getUserProfile(Session.getInstance().getUser().id);
        getProfileCall.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                //Log.e(TAG,response.body().toString());
                if (!getProfileCall.isCanceled()) {
                    try {
                        if (response.body().status == 1) {
                            progressDialog.cancel();
                            userName.setText(response.body().user.user_name);
                            userPoints.setText(response.body().user.user_points);
                            userCards.setText(response.body().user.user_cards);
                            userGarages.setText(response.body().user.user_garages);
                            userEmail.setText(response.body().user.user_email);
                            userPhone.setText(response.body().user.phone_number);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }


            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                if (!getProfileCall.isCanceled()) {
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("My Profile");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getProfileCall != null) {
            getProfileCall.cancel();
        }
    }
}
