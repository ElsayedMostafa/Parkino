package com.example.madara.parkino;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 */
public class ChargeFragment extends Fragment implements View.OnClickListener{
    private CardView cardOne, cardTwo, cardThree, cardFour;

    public ChargeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_charge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardOne = view.findViewById(R.id.charge_10_points_card);
        cardTwo = view.findViewById(R.id.charge_25_points_card);
        cardThree = view.findViewById(R.id.charge_50_points_card);
        cardFour = view.findViewById(R.id.charge_100_points_card);
        //
        cardOne.setOnClickListener(this);
        cardTwo.setOnClickListener(this);
        cardThree.setOnClickListener(this);
        cardFour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),CreditScreen.class);
        switch (v.getId()) {

            case R.id.charge_10_points_card:
                intent.putExtra("card_id","10");
                startActivity(intent);
                break;
            case R.id.charge_25_points_card:
                intent.putExtra("card_id","25");
                startActivity(intent);
                break;
            case R.id.charge_50_points_card:
                intent.putExtra("card_id","50");
                startActivity(intent);
                break;
            case R.id.charge_100_points_card:
                intent.putExtra("card_id","100");
                startActivity(intent);
                break;
        }
    }
    

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("Charge");
    }

}
