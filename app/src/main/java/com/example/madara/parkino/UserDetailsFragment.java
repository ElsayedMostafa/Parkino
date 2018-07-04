package com.example.madara.parkino;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment {
    private TextView userSlot,userRfid, userType;

    public UserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userSlot = view.findViewById(R.id.user_details_slot);
        userRfid = view.findViewById(R.id.user_details_rfid);
        userType = view.findViewById(R.id.user_details_type);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userSlot.setText(bundle.getString("user_slot"));
            userRfid.setText(bundle.getString("user_card"));
            userType.setText(bundle.getString("type"));
        }
    }
}
