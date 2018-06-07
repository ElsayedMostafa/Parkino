package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 3/28/18.
 */

public class CardResponse {
    @SerializedName("user_id")
    public int user_id;
    @SerializedName("card_no")
    public String mId;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("updated_at")
    public String updated_at;



    public CardResponse(String id){
        this.mId = id;
    }
    public String getId(){
        return this.mId;
    }
}
