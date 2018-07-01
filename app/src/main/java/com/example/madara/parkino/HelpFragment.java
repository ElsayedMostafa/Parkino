package com.example.madara.parkino;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {
    private LinearLayout linearLayoutSendFeedback;
    private TextView helpChangePassword, helpAddRemoveCard, helpImproveLocation, helpChargePoints, helpReserveCancel;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helpChangePassword = view.findViewById(R.id.help_change_reset_password);
        helpAddRemoveCard = view.findViewById(R.id.help_add_or_remove_card);
        helpImproveLocation = view.findViewById(R.id.help_improve_location_accuracy);
        helpChargePoints = view.findViewById(R.id.help_charge_points);
        helpReserveCancel = view.findViewById(R.id.help_reserve_cancel);
        linearLayoutSendFeedback = view.findViewById(R.id.help_send_feedback);
        linearLayoutSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendFeedback.class));
            }
        });
        final Intent helpUser = new Intent(getActivity(),HelpUser.class);
        helpChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpUser.putExtra("actionbar","Change Password");
                helpUser.putExtra("check","1");
                startActivity(helpUser);
            }
        });
        helpAddRemoveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpUser.putExtra("actionbar","Add or Remove Card");
                helpUser.putExtra("check","2");
                startActivity(helpUser);
            }
        });
        helpImproveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpUser.putExtra("actionbar","Improve Location");
                helpUser.putExtra("check","3");
                startActivity(helpUser);
            }
        });
        helpChargePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpUser.putExtra("actionbar","Charge Points");
                helpUser.putExtra("check","4");
                startActivity(helpUser);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("Help");
    }
}
