package com.example.madara.parkino.dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.madara.parkino.R;
import com.example.madara.parkino.UserCardsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by madara on 3/27/18.
 */

public class GetPassword extends DialogFragment {
    private static final String TAG = "GetPasswordFragment";
    private EditText mPassword;
    private Button ok,cancel;
    private PasswordListener listener;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_get_password,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPassword = (EditText) view.findViewById(R.id.et_get_password);
        ok = (Button) view.findViewById(R.id.btn_action_ok);
        cancel = (Button) view.findViewById(R.id.btn_action_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                listener.cancelPassword();



            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getText().toString();
                if(password.trim().isEmpty()){
                    mPassword.setError("Enter your Password");
                }
                else{
                int key = getArguments().getInt("num");
                getDialog().dismiss();
                listener.getPassword(password,key);
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PasswordListener) getTargetFragment();
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement password listener");
        }

    }


    public interface PasswordListener{
        void getPassword(String password, int key);
        void cancelPassword();
    }

}
