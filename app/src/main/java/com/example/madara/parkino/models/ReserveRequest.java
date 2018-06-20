package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 6/20/18.
 */

public class ReserveRequest {
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("garage_id")
    public String garage_id;
    @SerializedName("password")
    public String user_password;

}
