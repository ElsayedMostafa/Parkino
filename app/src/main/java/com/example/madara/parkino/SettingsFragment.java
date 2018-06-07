package com.example.madara.parkino;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingsFragment extends Fragment implements View.OnClickListener{
    private ImageView nameSettingButton, emailSettingButton, passwordSettingButton, phoneSettingButton;
    private TextView nameSetting, emailSetting, passwordSetting, phoneSetting;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameSetting = (TextView) view.findViewById(R.id.tv_setting_name);
        emailSetting = (TextView) view.findViewById(R.id.tv_setting_email);
        passwordSetting = (TextView) view.findViewById(R.id.tv_setting_password);
        phoneSetting = (TextView) view.findViewById(R.id.tv_setting_phone);
        nameSettingButton = (ImageView) view.findViewById(R.id.img_setting_name);
        emailSettingButton = (ImageView) view.findViewById(R.id.img_setting_email);
        passwordSettingButton = (ImageView) view.findViewById(R.id.img_setting_password);
        phoneSettingButton = (ImageView) view.findViewById(R.id.img_setting_phone);
        nameSettingButton.setOnClickListener(this);
        emailSettingButton.setOnClickListener(this);
        passwordSettingButton.setOnClickListener(this);
        phoneSettingButton.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("Settings");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_setting_name:
                Intent name_intent = new Intent(getActivity(),EditSettings.class);
                name_intent.putExtra("check",0);
                name_intent.putExtra("action_value","Enter your name");
                name_intent.putExtra("Value",nameSetting.getText().toString());
                startActivity(name_intent);
                break;
            case R.id.img_setting_email:
                Intent email_intent = new Intent(getActivity(),EditSettings.class);
                email_intent.putExtra("check",1);
                email_intent.putExtra("action_value","Enter your email");
                email_intent.putExtra("Value",emailSetting.getText().toString());
                startActivity(email_intent);
                break;
            case R.id.img_setting_password:
                Intent password_intent = new Intent(getActivity(),EditSettings.class);
                password_intent.putExtra("check",2);
                password_intent.putExtra("action_value","Enter your password");
                password_intent.putExtra("Value","");
                startActivity(password_intent);
                break;
            case R.id.img_setting_phone:
                Intent phone_intent = new Intent(getActivity(),EditSettings.class);
                phone_intent.putExtra("check",3);
                phone_intent.putExtra("action_value","Enter your phone number");
                phone_intent.putExtra("Value",phoneSetting.getText().toString());
                startActivity(phone_intent);
                break;
        }

    }
}
