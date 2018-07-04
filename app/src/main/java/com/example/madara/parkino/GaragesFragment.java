package com.example.madara.parkino;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madara.parkino.adapters.CardAdapter;
import com.example.madara.parkino.adapters.GarageAdapter;
import com.example.madara.parkino.models.CardResponse;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.GarageRequest;
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
public class GaragesFragment extends Fragment {
    private final static String TAG = "GaragesFragment";
    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    private Call<List<Garage>> getGaragesCall;
    private Call<List<CardResponse>> getCardsCall;
    private List<Garage> garages = new ArrayList<Garage>();
    private List<CardResponse> cards = new ArrayList<CardResponse>();
    private GarageAdapter garageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    LocationManager lm ;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private String lat,lng;
    private ArrayList <String> rfids = new ArrayList<String>();
    String url = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=mobilechallenge-close-up-dew-807598.jpg&fm=jpg";
    public GaragesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        rfids.add("Select Card");
        recyclerView = view.findViewById(R.id.garages_recyclerview);
        progressBar = view.findViewById(R.id.garages_progressbar);
        swipeRefreshLayout = view.findViewById(R.id.garages_refresh);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                if(network_enabled==true || gps_enabled == true){
                    if(getGaragesCall==null){
                        //30.8246818,30.9918712
                        getGarages("30.9918712","30.8246818");
                    }}
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        checkGps();
        if(getCardsCall==null){
            getCards();
        }

    }
    private void getGarages(String lng, String lat){
        progressBar.setVisibility(View.VISIBLE);
        GarageRequest garagerequest = new GarageRequest();
        garagerequest.user_id =  Session.getInstance().getUser().id;
        garagerequest.latitude = lat;
        garagerequest.longitude = lng;
        getGaragesCall = WebService.getInstance().getApi().getGarages(garagerequest);
        getGaragesCall.enqueue(new Callback<List<Garage>>() {
            @Override
            public void onResponse(Call<List<Garage>> call, Response<List<Garage>> response) {
                //Log.e(TAG,response.body().toString());
                if(!getGaragesCall.isCanceled()){
                try {
                    garages = response.body();
                    garageAdapter = new GarageAdapter(garages,getActivity(),rfids);
                    recyclerView.setAdapter(garageAdapter);
                    progressBar.setVisibility(View.GONE);
                    getGaragesCall = null;
                }catch (Exception e){
                    if(getActivity()!=null){
                    Toast.makeText(getActivity(), "Failed to get garages", Toast.LENGTH_LONG).show();}
                    progressBar.setVisibility(View.GONE);
                    getGaragesCall = null;

                }}
            }
            @Override
            public void onFailure(Call<List<Garage>> call, Throwable t) {
                if(!getGaragesCall.isCanceled()) {
                    //30.787069, 31.000721
                    progressBar.setVisibility(View.GONE);
                    if(getActivity()!=null){
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();}
//                    garages.add(new Garage("123","Anwar Al Madinah","30.787069","31.000721",url,"0.6666", "20","3", 4f,9));
//                    garages.add(new Garage("123","TownTeam","30.787069","31.000721",url,"0.6666", "30","3", 4f,9));
//                    garages.add(new Garage("123","Mahalla","30.787069","31.000721",url,"0.6666", "40","3", 4f,9));
//                    garageAdapter = new GarageAdapter(garages,getActivity(),rfids);
//                    recyclerView.setAdapter(garageAdapter);
                    getGaragesCall = null;
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().isEmpty()){
                    Intent searchIntent = new Intent(getActivity(),SearchResultsActivity.class);
                    searchIntent.putExtra("search_text",query);
                    searchIntent.putExtra("lat",lat);
                    searchIntent.putExtra("long",lng);
                    startActivity(searchIntent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(garageAdapter!=null)
                garageAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
    private void getCards() {
        //progressBar.setVisibility(View.VISIBLE);
        getCardsCall = WebService.getInstance().getApi().getCards(Session.getInstance().getUser().id);
        getCardsCall.enqueue(new Callback<List<CardResponse>>() {
            @Override
            public void onResponse(Call<List<CardResponse>> call, Response<List<CardResponse>> response) {
                if (!getCardsCall.isCanceled()) {
                    try {
                        cards = response.body();
                        for(CardResponse iter:cards){
                            rfids.add(iter.mId);
                        }
                        //progressBar.setVisibility(View.GONE);
                        getCardsCall=null;
                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        if(getActivity()!=null){
                        //Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                             }
                        getCardsCall=null;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardResponse>> call, Throwable t) {
                if (!getCardsCall.isCanceled()) {
                    //progressBar.setVisibility(View.GONE);
                    if(getActivity()!=null) {
                        //Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
                    }
                    getCardsCall=null;
//                   cards.add(new CardResponse("123456789123"));
//                    cards.add(new CardResponse("123456789123"));
//                    cards.add(new CardResponse("123456225685"));
//                    cards.add(new CardResponse("123456700023"));
//                    cardAdapter = new CardAdapter(cards, getActivity());
//                    recyclerView.setAdapter(cardAdapter);
                }
            }
        });

    }
    private void checkGps(){
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(getActivity().getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getActivity().getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getContext().startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(getActivity().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("Find Garage");
        if(network_enabled==true || gps_enabled == true){
            if(getGaragesCall==null){
            getGarages("30.9918712","30.8246818");
        }}

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getGaragesCall!=null){
            getGaragesCall.cancel();
        }
        else if (getCardsCall != null) {
            getCardsCall.cancel();
        }

    }
}
