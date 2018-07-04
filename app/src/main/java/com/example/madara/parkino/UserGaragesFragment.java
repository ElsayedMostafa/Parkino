package com.example.madara.parkino;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madara.parkino.adapters.GarageAdapter;
import com.example.madara.parkino.adapters.UserGarageAdapter;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.GarageRequest;
import com.example.madara.parkino.models.UserGarage;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserGaragesFragment extends Fragment {
    private final static String TAG = "UserGaragesFragment";
    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    private Call<List<UserGarage>> getUserGaragesCall;
    private List<UserGarage> userGarages = new ArrayList<UserGarage>();
    private UserGarageAdapter userGarageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    String url = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=mobilechallenge-close-up-dew-807598.jpg&fm=jpg";


    public UserGaragesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_garages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.user_garages_recyclerview);
        progressBar = view.findViewById(R.id.user_garages_progressbar);
        swipeRefreshLayout = view.findViewById(R.id.user_garages_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                if(getUserGaragesCall==null){
                    getUserGarages("30.9918712","30.8246818");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void getUserGarages(String lng, String lat){
        progressBar.setVisibility(View.VISIBLE);
        GarageRequest garagerequest = new GarageRequest();
        garagerequest.user_id =  Session.getInstance().getUser().id;
        garagerequest.latitude = lat;
        garagerequest.longitude = lng;
        getUserGaragesCall = WebService.getInstance().getApi().getUserGarages(garagerequest);
        getUserGaragesCall.enqueue(new Callback<List<UserGarage>>() {
            @Override
            public void onResponse(Call<List<UserGarage>> call, Response<List<UserGarage>> response) {
                //Log.e(TAG,response.body().toString());
                if(!getUserGaragesCall.isCanceled()){
                    try {
                        userGarages = response.body();
                        userGarageAdapter = new UserGarageAdapter(userGarages,getActivity());
                        recyclerView.setAdapter(userGarageAdapter);
                        progressBar.setVisibility(View.GONE);
                        getUserGaragesCall = null;
                    }catch (Exception e){
                        Log.e(TAG,"failed to get garages");
                        progressBar.setVisibility(View.GONE);
                        getUserGaragesCall = null;

                    }}
            }
            @Override
            public void onFailure(Call<List<UserGarage>> call, Throwable t) {
                if(!getUserGaragesCall.isCanceled()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
//                    userGarages.add(new Garage("123","Anwar Al Madinah","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    userGarages.add(new Garage("123","TownTeam","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    userGarages.add(new Garage("123","Mahalla","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    userGarageAdapter = new UserGarageAdapter(userGarages,getActivity());
//                    recyclerView.setAdapter(userGarageAdapter);
                    getUserGaragesCall = null;
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("My Garages");
        if(getUserGaragesCall==null){
            getUserGarages("30.9918712","30.8246818");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getUserGaragesCall!=null){
            getUserGaragesCall.cancel();
        }
    }
}
