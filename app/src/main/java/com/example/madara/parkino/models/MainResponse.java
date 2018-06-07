package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 2/22/18.
 */

public class MainResponse {

    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
}
