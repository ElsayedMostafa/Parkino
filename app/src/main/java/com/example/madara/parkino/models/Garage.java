package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 3/12/18.
 */

public class Garage {
    @SerializedName("garage_id")
    public String id;
    @SerializedName("garage_name")
    public String name;
    @SerializedName("latitude")
    public String lat;
    @SerializedName("longitude")
    public String lng;
    @SerializedName("image")
    public String image;
    @SerializedName("distance")
    public String distance;
    @SerializedName("slotnumbers")
    public String slotnumbers;
    @SerializedName("price")
    public String price;
    @SerializedName("stars")
    public float stars;
    @SerializedName("emptyslots")
    public int emptyslots;

    public Garage(String id,String name, String lat, String lng,
                  String image, String distance, String slotnumbers, String price, float stars, int emptyslots){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
        this.distance = distance;
        this.slotnumbers = slotnumbers;
        this.price = price;
        this.stars = stars;
        this.emptyslots = emptyslots;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getImage() {
        return image;
    }

    public String getDistance() {
        return distance;
    }

    public String getSlotsnumber() {
        return slotnumbers;
    }

    public String getPrice() {
        return price;
    }

    public float getStars() {
        return stars;
    }

    public int getEmptyslots() {
        return emptyslots;
    }
}
