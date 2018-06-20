package com.example.madara.parkino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madara.parkino.adapters.GarageAdapter;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.GarageRequest;
import com.example.madara.parkino.models.SearchRequest;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.WebService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity {
    private final static String TAG = "GaragesFragment";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Call<List<Garage>> searchGaragesCall;
    private List<Garage> garages = new ArrayList<Garage>();
    private GarageAdapter garageAdapter;
    private int id;
    String url = "https://images.pexels.com/photos/807598/pexels-photo-807598.jpeg?cs=srgb&dl=mobilechallenge-close-up-dew-807598.jpg&fm=jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = findViewById(R.id.search_recyclerview);
        progressBar = findViewById(R.id.search_progressbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchResultsActivity.this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Garage");
        if (getIntent() != null) {
            String search_text = getIntent().getStringExtra("search_text");
            String lat = getIntent().getStringExtra("lat");
            String lng = getIntent().getStringExtra("long");
            id = Session.getInstance().getUser().id;
            searchGarages(id, search_text, "123", "123");
        }

    }

    private void searchGarages(int user_id, String search_text, String lng, String lat) {
        progressBar.setVisibility(View.VISIBLE);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.user_id = user_id;
        searchRequest.searchText = search_text;
        searchRequest.latitude = lat;
        searchRequest.longitude = lng;
        searchGaragesCall = WebService.getInstance().getApi().searchGarages(searchRequest);
        searchGaragesCall.enqueue(new Callback<List<Garage>>() {
            @Override
            public void onResponse(Call<List<Garage>> call, Response<List<Garage>> response) {
                //Log.e(TAG,response.body().toString());
                if (!searchGaragesCall.isCanceled()) {
                    try {
                        garages = response.body();
                        garageAdapter = new GarageAdapter(garages, SearchResultsActivity.this);
                        recyclerView.setAdapter(garageAdapter);
                        progressBar.setVisibility(View.GONE);
                        searchGaragesCall = null;
                    } catch (Exception e) {
                        Toast.makeText(SearchResultsActivity.this, "Failed to get garages", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        searchGaragesCall = null;

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Garage>> call, Throwable t) {
                if (!searchGaragesCall.isCanceled()) {
                    //30.787069, 31.000721
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SearchResultsActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
//                    garages.add(new Garage("123","Anwar Al Madinah","30.787069","31.000721",url,"0.6", "20","3", 4f,9));
//                    garages.add(new Garage("123","TownTeam","30.787069","31.000721",url,"0.6", "30","3", 4f,9));
//                    garages.add(new Garage("123","Mahalla","30.787069","31.000721",url,"0.6", "40","3", 4f,9));
//                    garageAdapter = new GarageAdapter(garages,SearchResultsActivity.this);
//                    recyclerView.setAdapter(garageAdapter);
                    searchGaragesCall = null;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.trim().isEmpty()) {
                    searchGarages(id, s, "123", "123");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (garageAdapter != null)
                    garageAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (searchGaragesCall != null) {
            searchGaragesCall.cancel();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
