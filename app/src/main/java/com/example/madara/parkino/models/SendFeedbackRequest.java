package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 6/18/18.
 */

public class SendFeedbackRequest {
    @SerializedName("user_id")
    public int id;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_message")
    public String userMessage;

}
