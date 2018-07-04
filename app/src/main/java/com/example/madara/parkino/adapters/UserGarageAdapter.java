package com.example.madara.parkino.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madara.parkino.GarageProfile;
import com.example.madara.parkino.R;
import com.example.madara.parkino.UserGarageProfile;
import com.example.madara.parkino.models.CancelGarageRequest;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.ReserveRequest;
import com.example.madara.parkino.models.UserGarage;
import com.example.madara.parkino.utils.Session;
import com.example.madara.parkino.webservices.Urls;
import com.example.madara.parkino.webservices.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by madara on 3/12/18.
 */

public class UserGarageAdapter extends RecyclerView.Adapter<UserGarageAdapter.GarageHolder> implements Filterable {
    private static final String TAG = "UserGarageAdapter";
    private List<UserGarage> garageList;
    private List<UserGarage> garageListFull;
    private Context context;
    Dialog cancelDialog;
    TextView diaglogCancelGarageName;
    EditText dialogCancelPassword;
    Button buttonStart;
    private Call <MainResponse> cancelGarageRequestCall;
    public UserGarageAdapter(List<UserGarage> garageList , Context ctx) {
        this.garageList = garageList;
        garageListFull = new ArrayList<>(garageList);
        this.context = ctx;
    }

    @NonNull
    @Override
    public GarageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_garage_row, parent, false);
        GarageHolder holder = new GarageHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GarageHolder holder, int position) {
        final UserGarage garage = garageList.get(position);
        holder._garageName.setText(garage.getName());
        final double dis = Math.round(Double.valueOf(garage.getDistance()) * 100.0) / 100.0;
        holder._garageDistance.setText(String.valueOf(dis) + " km from centre");
        holder._garageId.setText(garage.getId());
        holder._slotsNumbers.setText(String.valueOf(garage.getSlotnumbers())+" Slots");
        holder._emptySlots.setText(String.valueOf(garage.getEmptyslots()));
        if(garage.getAnnually_tier().equals("1")){
            holder._points.setText("100 points/year");
        }
        else if(garage.getMonthly_tier().equals("1")){
            holder._points.setText("20 points/month");
        }
        else if(garage.getDaily_tier().equals("1")){
            holder._points.setText("10 points/day");
        }
        else if(garage.getHourly_tier().equals("1")){
            holder._points.setText("1 point/hour");

        }
        holder._lng.setText(garage.getLng());
        holder._lat.setText(garage.getLat());
        holder._stars.setRating(garage.getStars());
        //156.217.47.80:8000
        Picasso.get().load("http://"+Urls.PHOTO_URL+"/garagePhotosFolder/2/profile.png").resize(108,108).into(holder._image);
        holder.garageRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserGarageProfile.class);
                intent.putExtra("garageDistance", String.valueOf(dis));
                intent.putExtra("garageImage", garage.getImage());
                intent.putExtra("garageName", garage.getName());
                intent.putExtra("garageId", garage.getId());
                intent.putExtra("slotsNumbers", garage.getSlotnumbers());
                intent.putExtra("emptySlots", String.valueOf(garage.getEmptyslots()));
                intent.putExtra("points", garage.getPrice());
                intent.putExtra("stars", garage.stars);
                intent.putExtra("lat", garage.getLat());
                intent.putExtra("long", garage.getLng());
                intent.putExtra("user_card",garage.getUserCard());
                intent.putExtra("user_slot",garage.getUserSlot());
                if(garage.getAnnually_tier().equals("1")){
                    intent.putExtra("type","100 points/year");
                }
                else if(garage.getMonthly_tier().equals("1")){
                    intent.putExtra("type","20 points/month");
                }
                else if(garage.getDaily_tier().equals("1")){
                    intent.putExtra("type","10 points/day");
                }
                else if(garage.getHourly_tier().equals("1")){
                    intent.putExtra("type","1 point/hour");

                }
                context.startActivity(intent);
            }
        });
        holder._btn_cancelgarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog = new Dialog(context);
                cancelDialog.setContentView(R.layout.dialog_cancel);
                cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                diaglogCancelGarageName = cancelDialog.findViewById(R.id.dialog_cancel_garage_name);
                dialogCancelPassword = cancelDialog.findViewById(R.id.dialog_cancel_password);
                buttonStart = cancelDialog.findViewById(R.id.dialog_cancel_start);
                diaglogCancelGarageName.setText(garageList.get(holder.getAdapterPosition()).getName());
                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password;
                        password = dialogCancelPassword.getText().toString();
                        if(password.isEmpty()){
                            dialogCancelPassword.setError("Enter your password");
                        }
                        else{
                            CancelGarageRequest cancelGarageRequest = new CancelGarageRequest();
                            cancelGarageRequest.userId = Session.getInstance().getUser().id;
                            cancelGarageRequest.garage_id = garageList.get(holder.getAdapterPosition()).getId();
                            cancelGarageRequest.userPassword = password;
                            cancelGarageRequest.userSlot = garage.userSlot;
                            cancelGarage(cancelGarageRequest);

                        }
                    }
                });
                cancelDialog.show();

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
        Button _btn_cancelgarage;
        CardView garageRow;
        public GarageHolder(View itemView) {
            super(itemView);
            garageRow = (CardView) itemView.findViewById(R.id.garage_row_id);
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
            _btn_cancelgarage = (Button) itemView.findViewById(R.id.btn_cancelgarage);

        }
    }
    @Override
    public Filter getFilter() {
        return garageFilter;
    }
    private Filter garageFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserGarage> filteredList = new ArrayList<>();
            if(constraint == null|| constraint.length() ==0){
                filteredList.addAll(garageListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(UserGarage garage:garageListFull){
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
    public void cancelGarage(CancelGarageRequest cancelGarageRequest) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Cancel...");
        progressDialog.show();
        cancelGarageRequestCall = WebService.getInstance().getApi().cancelGarage(cancelGarageRequest);
        cancelGarageRequestCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (!cancelGarageRequestCall.isCanceled()) {
                    try {
                        if (response.body().status == 1) {
                            progressDialog.cancel();
                            Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            cancelGarageRequestCall = null;
                            cancelDialog.cancel();
                        }
                        if (response.body().status == 0) {
                            progressDialog.cancel();
                            Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            cancelGarageRequestCall = null;
                            cancelDialog.cancel();
                        }
                    } catch (Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        cancelGarageRequestCall = null;
                        cancelDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                if (!cancelGarageRequestCall.isCanceled()) {
                    progressDialog.cancel();
                    Toast.makeText(context, "Check Network Connection", Toast.LENGTH_LONG).show();
                    cancelGarageRequestCall = null;
                    cancelDialog.cancel();
                }

            }
        });
    }
}
