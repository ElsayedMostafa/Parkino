package com.example.madara.parkino.webservices;



import com.example.madara.parkino.models.Card;
import com.example.madara.parkino.models.CardResponse;
import com.example.madara.parkino.models.ChargeRequest;
import com.example.madara.parkino.models.Garage;
import com.example.madara.parkino.models.GarageRequest;
import com.example.madara.parkino.models.LoginResponse;
import com.example.madara.parkino.models.MainResponse;
import com.example.madara.parkino.models.ReserveRequest;
import com.example.madara.parkino.models.SearchRequest;
import com.example.madara.parkino.models.SendFeedbackRequest;
import com.example.madara.parkino.models.User;
import com.example.madara.parkino.models.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by madara on 2/22/18.
 */

public interface Api {
    @Headers("content-type: application/json")
    //@POST("login")
    @POST("login-user.php")
    Call<LoginResponse> loginUser(@Body User user);
//
//    //@POST("register-user.php")
    @POST("register")
    Call<MainResponse> registerUser(@Body User user);
    @POST("bindcard")
    Call<MainResponse> bindCard(@Body Card card);
    @FormUrlEncoded
    @POST("getMyCards")
    Call<List<CardResponse>> getCards(@Field("user_id") int user_id);
    @POST("unbindcard")
    Call<MainResponse> unbindcard(@Body Card card);
      @FormUrlEncoded
    @POST("userProfileData")
    Call<UserProfileResponse> getUserProfile(@Field("userid") int user_id);

    @POST("getGarages")
    Call<List<Garage>> getGarages(@Body GarageRequest garageRequest);
    @POST("getUserGarages")
    Call<List<Garage>> getUserGarages(@Body GarageRequest garageRequest);
//    //edit user information
    @POST("ChangeUsername")
    Call<MainResponse> changeName(@Body User user);
    @POST("ChangeEmail")
    Call<MainResponse> changeEmail(@Body User user);
    @POST("ChangePassword")
    Call<MainResponse> changePassword(@Body User user);
    @POST("ChangePhoneNumber")
    Call<MainResponse> changePhone(@Body User user);
    @POST("charge")
    Call<MainResponse> charge (@Body ChargeRequest chargeRequest);
    @POST("feedback")
    Call<MainResponse> feedback (@Body SendFeedbackRequest sendFeedbackRequest);
    @POST("reserveGarage")
    Call<MainResponse> reserveGarage(@Body ReserveRequest reserveRequest);
    @POST("searchGarages")
    Call<List<Garage>> searchGarages(@Body SearchRequest searchRequest);
}
