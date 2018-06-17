package com.example.madara.parkino.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.madara.parkino.GarageProfile;
import com.example.madara.parkino.R;
import com.example.madara.parkino.models.Garage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madara on 3/12/18.
 */

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageHolder> implements Filterable {
    private static final String TAG = "GarageAdapter";
    private List<Garage> garageList;
    private List<Garage> garageListFull;
    private Context context;
    public GarageAdapter(List<Garage> garageList , Context ctx) {
        this.garageList = garageList;
        garageListFull = new ArrayList<>(garageList);
        this.context = ctx;
    }

    @NonNull
    @Override
    public GarageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_row, parent, false);
        GarageHolder holder = new GarageHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GarageHolder holder, int position) {
        final Garage garage = garageList.get(position);
        holder._garageName.setText(garage.getName());
        holder._garageDistance.setText(garage.getDistance());
        holder._garageId.setText(garage.getId());
        holder._slotsNumbers.setText(String.valueOf(garage.getSlotsnumber()));
        holder._emptySlots.setText(String.valueOf(garage.getEmptyslots()));
        holder._points.setText(garage.getPrice());
        holder._lng.setText(garage.getLng());
        holder._lat.setText(garage.getLat());
        holder._stars.setRating(garage.getStars());
        Picasso.get().load(garage.getImage()).resize(108,108).into(holder._image);
        holder._btn_opengarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GarageProfile.class);
                intent.putExtra("garageimgae",garage.getImage());
                intent.putExtra("garagename",garage.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return garageList.size();
    }



    class GarageHolder extends RecyclerView.ViewHolder {
        TextView _garageName, _garageDistance, _garageId, _slotsNumbers, _emptySlots, _points, _lng, _lat;
        RatingBar _stars;
        ImageView _image;
        Button _btn_opengarage;
        public GarageHolder(View itemView) {
            super(itemView);
            _garageId = (TextView) itemView.findViewById(R.id.garage_id);
            _garageName = (TextView) itemView.findViewById(R.id.garage_name);
            _garageDistance = (TextView) itemView.findViewById(R.id.garage_distance);
            _slotsNumbers =(TextView) itemView.findViewById(R.id.slots_number);
            _emptySlots = (TextView) itemView.findViewById(R.id.empty_slots);
            _points = (TextView) itemView.findViewById(R.id.points);
            _lng = (TextView) itemView.findViewById(R.id.garage_lng);
            _lat = (TextView) itemView.findViewById(R.id.garage_lat);
            _stars = (RatingBar) itemView.findViewById(R.id.rating);
            _image = (ImageView) itemView.findViewById(R.id.garage_image);
            _btn_opengarage = (Button) itemView.findViewById(R.id.btn_opengarage);

        }
    }
    @Override
    public Filter getFilter() {
        return garageFilter;
    }
    private Filter garageFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Garage> filteredList = new ArrayList<>();
            if(constraint == null|| constraint.length() ==0){
                filteredList.addAll(garageListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Garage garage:garageListFull){
                    if(garage.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(garage);
                    }

                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            garageList.clear();
            garageList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
