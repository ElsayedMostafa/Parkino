package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 6/20/18.
 */

public class SearchRequest {
    @SerializedName("Search_text")
    public String searchText;
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("latitude")
    public String latitude;
    @SerializedName("longitude")
    public String longitude;
}
