package com.example.madara.parkino;


import android.os.Bundle;
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

import com.example.madara.parkino.adapters.GarageAdapter;
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
    private List<Garage> garages = new ArrayList<Garage>();
    private GarageAdapter garageAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        recyclerView = view.findViewById(R.id.garages_recyclerview);
        progressBar = view.findViewById(R.id.garages_progressbar);
        swipeRefreshLayout = view.findViewById(R.id.garages_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                if(getGaragesCall==null){
                    getGarages("123","123");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });



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
                    garageAdapter = new GarageAdapter(garages,getActivity());
                    recyclerView.setAdapter(garageAdapter);
                    progressBar.setVisibility(View.GONE);
                    getGaragesCall = null;
                }catch (Exception e){
                    Log.e(TAG,"failed to get garages");
                    progressBar.setVisibility(View.GONE);
                    getGaragesCall = null;

                }}
            }
            @Override
            public void onFailure(Call<List<Garage>> call, Throwable t) {
                if(!getGaragesCall.isCanceled()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
//                    garages.add(new Garage("123","Anwar Al Madinah","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    garages.add(new Garage("123","TownTeam","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    garages.add(new Garage("123","Mahalla","30.36","30.31",url,"0.6 km from centre", "40 Slots","3P", 4f,9));
//                    garageAdapter = new GarageAdapter(garages,getActivity());
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

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).getSupportActionBar().setTitle("Find Garage");
        if(getGaragesCall==null){
            getGarages("123","123");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getGaragesCall!=null){
            getGaragesCall.cancel();
        }
    }
}
