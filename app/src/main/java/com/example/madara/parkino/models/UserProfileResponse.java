package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 5/23/18.
 */

public class UserProfileResponse {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("user")
    public User user;
    public static class User {
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("user_email")
        public String user_email;
        @SerializedName("phone_number")
        public String phone_number;
        @SerializedName("points")
        public String user_points;
        @SerializedName("cards")
        public String user_cards;
        @SerializedName("garages")
        public String user_garages;

    }
}
