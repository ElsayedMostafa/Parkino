package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 5/4/18.
 */

public class GarageRequest {
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
}
