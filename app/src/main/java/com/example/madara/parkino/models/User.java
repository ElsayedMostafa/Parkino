package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by madara on 2/22/18.
 */

public class User extends RealmObject{
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("email")
    public String email;
    @SerializedName("phone_number")
    public String phone_number;
    @SerializedName("userid")
    public int id;
    @SerializedName("oldpassword")
    public String oldpassword;
}
