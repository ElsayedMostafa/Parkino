package com.example.madara.parkino;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    private TextView slotsNumbers,emptySlots,points,distance;
    public DetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slotsNumbers = view.findViewById(R.id.details_solts_number);
        emptySlots = view.findViewById(R.id.details_empty_solts);
        points = view.findViewById(R.id.details_points);
        distance = view.findViewById(R.id.details_distance);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            slotsNumbers.setText(bundle.getString("slotsNumbers")+" slots");
            emptySlots.setText(bundle.getString("emptySlots")+ " available slots");
            points.setText(bundle.getString("points")+ " point for hour");
            distance.setText(bundle.getString("garageDistance")+ " km from centre");
        }

    }
}
