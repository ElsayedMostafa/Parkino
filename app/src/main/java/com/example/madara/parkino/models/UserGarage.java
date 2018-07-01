package com.example.madara.parkino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madara on 3/12/18.
 */

public class UserGarage {
    @SerializedName("garage_id")
    public String id;
    @SerializedName("garage_name")
    public String name;
    @SerializedName("latitude")
    public String lat;
    @SerializedName("longitude")
    public String lng;
    @SerializedName("grageURL")
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
    @SerializedName("slot")
    public String userSlot;
    @SerializedName("card_id")
    public String userCard;
    @SerializedName("annually_tier")
    public String annually_tier;
    @SerializedName("monthly_tier")
    public String monthly_tier;
    @SerializedName("daily_tier")
    public String daily_tier;
    @SerializedName("hourly_tier")
    public String hourly_tier;
    public UserGarage(String id, String name, String lat, String lng,
                      String image, String distance, String slotnumbers, String price, float stars, int emptyslots,
                      String user_slot, String user_card, String annually_tier, String monthly_tier,
                      String daily_tier, String hourly_tier){
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
        this.userSlot = user_slot;
        this.userCard = user_card;
        this.annually_tier = annually_tier;
        this.monthly_tier = monthly_tier;
        this.daily_tier = daily_tier;
        this.hourly_tier = hourly_tier;
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

    public String getSlotnumbers() {
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

    public String getUserSlot() {
        return userSlot;
    }

    public String getUserCard() {
        return userCard;
    }

    public String getAnnually_tier() {
        return annually_tier;
    }

    public String getMonthly_tier() {
        return monthly_tier;
    }

    public String getDaily_tier() {
        return daily_tier;
    }

    public String getHourly_tier() {
        return hourly_tier;
    }
}
