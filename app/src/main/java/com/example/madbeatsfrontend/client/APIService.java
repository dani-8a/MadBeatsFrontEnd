package com.example.madbeatsfrontend.client;

import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("/api/spots")
    Call<List<Spot>> getAllSpots();

    @GET("/api/events")
    Call<List<Event>> getAllEvents();

    @GET("/api/events/{eventId}")
    Call<Event> getEventInfo(@Path("eventId") String eventId);

    @GET("/api/spots/{spotId}/events")
    Call<SpotWithEventResponse> getSpotWithEvents(@Path("spotId") String spotId);

    @GET("/api/spots/spotsByMusicCategory/{musicCategory}")
    Call<List<Spot>> getSpotsByMusicCategory(@Path("musicCategory") String musicCategory);

    @GET("/api/spots/spotsByEventDate/{day}/{month}/{year}")
    Call<List<Spot>> getSpotsByEventDate(@Path("day") int day, @Path("month") int month, @Path("year") int year);

    @FormUrlEncoded
    @POST("/api/default_user/login")
    Call<Void> loginUser(@FieldMap Map<String, String> fields);

}
