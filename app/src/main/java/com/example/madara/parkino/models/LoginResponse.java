package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 2/22/18.
 */

public class LoginResponse {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;
    @SerializedName("user")
    public User user;
    public static class User {
        @SerializedName("user_id")
        public int id;
        @SerializedName("user_name")
        public String user_name;
        @SerializedName("user_email")
        public String user_email;
        @SerializedName("user_password")
        public String password;
        @SerializedName("phone_number")
        public String phone_number;

    }
}
