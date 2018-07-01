package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 6/30/18.
 */

public class CancelGarageRequest {
    @SerializedName("user_id")
    public int userId;
    @SerializedName("password")
    public String userPassword;
    @SerializedName("slot_num")
    public String userSlot;
    @SerializedName("garage_id")
    public String garage_id;

}
