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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.madara.parkino.GarageProfile;
import com.example.madara.parkino.LoginScreen;
import com.example.madara.parkino.R;
import com.example.madara.parkino.models.Card;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.ReserveRequest;
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

public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageHolder> implements Filterable {
    private static final String TAG = "GarageAdapter";
    private List<Garage> garageList;
    private List<Garage> garageListFull;
    private Context context;
    private Call<MainResponse> reserveGarageCall;
    Dialog reserveDialog;
    TextView diaglogReserveGarageName;
    EditText dialogReservePassword;
    Button buttonStart;
    Spinner reserveTypeSpinner, reserveRfidSpinner;
    private int selectedPackage;
    private List<String> userRfids;
    private String rfidcard;

    public GarageAdapter(List<Garage> garageList, Context ctx, List<String> rfids) {
        this.garageList = garageList;
        garageListFull = new ArrayList<>(garageList);
        this.context = ctx;
        this.userRfids = rfids;
    }

    @NonNull
    @Override
    public GarageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.garage_row, parent, false);
        GarageHolder holder = new GarageHolder(row);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GarageHolder holder, int position) {
        final Garage garage = garageList.get(position);
        holder._garageName.setText(garage.getName());
        final double dis = Math.round(Double.valueOf(garage.getDistance()) * 100.0) / 100.0;
        holder._garageDistance.setText(String.valueOf(dis) + " km from centre");
        holder._garageId.setText(garage.getId());
        holder._slotsNumbers.setText(String.valueOf(garage.getSlotsnumber()) + " Slots");
        holder._emptySlots.setText(String.valueOf(garage.getEmptyslots()));
        holder._points.setText(garage.getPrice() + " points/hour");
        holder._lng.setText(garage.getLng());
        holder._lat.setText(garage.getLat());
        holder._stars.setRating(garage.getStars());
        //156.217.47.80:8000
        Picasso.get().load("http://" +Urls.PHOTO_URL+ "/garagePhotosFolder/2/profile.png").resize(108, 108).into(holder._image);
        holder.garageRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GarageProfile.class);

                intent.putExtra("garageDistance", String.valueOf(dis));
                intent.putExtra("garageImage", garage.getImage());
                intent.putExtra("garageName", garage.getName());
                intent.putExtra("garageId", garage.getId());
                intent.putExtra("slotsNumbers", garage.getSlotsnumber());
                intent.putExtra("emptySlots", String.valueOf(garage.getEmptyslots()));
                intent.putExtra("points", garage.getPrice());
                intent.putExtra("stars", garage.stars);
                intent.putExtra("lat", garage.getLat());
                intent.putExtra("long", garage.getLng());

                context.startActivity(intent);
            }
        });
        holder._btn_opengarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserveDialog = new Dialog(context);
                reserveDialog.setContentView(R.layout.dialog_reserve);
                reserveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                diaglogReserveGarageName = reserveDialog.findViewById(R.id.dialog_reserve_garage_name);
                dialogReservePassword = reserveDialog.findViewById(R.id.dialog_reserve_password);
                buttonStart = reserveDialog.findViewById(R.id.dialog_reserve_start);
                reserveTypeSpinner = reserveDialog.findViewById(R.id.reserve_type_spinner);
                reserveRfidSpinner = reserveDialog.findViewById(R.id.reserve_rfid_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.reserve_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reserveTypeSpinner.setAdapter(adapter);
                reserveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();
                        switch (text) {
                            case "Select Package":
                                selectedPackage = 999;
                                break;
                            case "1 point/hour":
                                selectedPackage = 111;
                                break;
                            case "10 points/day":
                                selectedPackage = 222;
                                break;
                            case "20 points/month":
                                selectedPackage = 333;
                                break;
                            case "100 points/year":
                                selectedPackage = 444;
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //rfid Spinner
                ArrayAdapter<String> rfidAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, userRfids);
                rfidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reserveRfidSpinner.setAdapter(rfidAdapter);
                reserveRfidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rfidcard = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                diaglogReserveGarageName.setText(garageList.get(holder.getAdapterPosition()).getName());
                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password;
                        password = dialogReservePassword.getText().toString();
                        if (password.isEmpty()) {
                            dialogReservePassword.setError("Enter your password");

                        } else if (selectedPackage == 999) {
                            Toast.makeText(context, "Choose Package", Toast.LENGTH_SHORT).show();
                        } else if (rfidcard.equals("Select Card")) {
                            Toast.makeText(context, "Choose Card", Toast.LENGTH_SHORT).show();

                        } else {
                            ReserveRequest reserveRequest = new ReserveRequest();
                            reserveRequest.rfid = rfidcard;
                            reserveRequest.selectedPackage = String.valueOf(selectedPackage);
                            reserveRequest.user_password = password;
                            reserveRequest.garage_id = garageList.get(holder.getAdapterPosition()).getId();
                            reserveRequest.user_id = Session.getInstance().getUser().id;
                            reserveGarage(reserveRequest);

                        }
                    }
                });
                reserveDialog.show();

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
        CardView garageRow;

        public GarageHolder(View itemView) {
            super(itemView);
            garageRow = (CardView) itemView.findViewById(R.id.garage_row_id);
            _garageId = (TextView) itemView.findViewById(R.id.garage_id);
            _garageName = (TextView) itemView.findViewById(R.id.garage_name);
            _garageDistance = (TextView) itemView.findViewById(R.id.garage_distance);
            _slotsNumbers = (TextView) itemView.findViewById(R.id.slots_number);
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
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(garageListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Garage garage : garageListFull) {
                    if (garage.getName().toLowerCase().contains(filterPattern)) {
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
            garageList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void reserveGarage(ReserveRequest reserveRequest) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Reserving...");
        progressDialog.show();
        reserveGarageCall = WebService.getInstance().getApi().reserveGarage(reserveRequest);
        reserveGarageCall.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (!reserveGarageCall.isCanceled()) {
                    try {
                        if (response.body().status == 1) {
                            progressDialog.cancel();
                            Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            reserveGarageCall = null;
                            reserveDialog.cancel();
                        }
                        if (response.body().status == 0) {
                            progressDialog.cancel();
                            Toast.makeText(context, response.body().message, Toast.LENGTH_LONG).show();
                            reserveGarageCall = null;
                            reserveDialog.cancel();
                        }
                    } catch (Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                        reserveGarageCall = null;
                        reserveDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                if (!reserveGarageCall.isCanceled()) {
                    progressDialog.cancel();
                    Toast.makeText(context, "Check Network Connection", Toast.LENGTH_LONG).show();
                    reserveGarageCall = null;
                    reserveDialog.cancel();
                }

            }
        });
    }

}
