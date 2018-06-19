package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 6/18/18.
 */

public class ChargeRequest {
    @SerializedName("user_id")
    public int id;
    @SerializedName("amount_id")
    public int amountId;
    @SerializedName("credit_number")
    public int creditNumber;
}
