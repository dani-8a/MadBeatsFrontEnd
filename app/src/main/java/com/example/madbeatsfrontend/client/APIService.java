package com.example.madbeatsfrontend.client;

import com.example.madbeatsfrontend.entity.DefaultUser;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("/api/default_user/login")
    Call<DefaultUser> loginUser(@Body DefaultUser user);

    @POST("/api/default_user/register")
    Call<ResponseBody> registerUser(@Body DefaultUser user);

    @GET("/api/default_user/{userId}/favourite_events")
    Call<List<Event>> getUserFavouriteEvents(@Path("userId")String userId);

    @GET("/api/default_user/{userId}/favourite_spots")
    Call<List<Spot>> getUserFavouriteSpots(@Path("userId")String userId);

    @DELETE("/api/default_user/{userId}/delete_all_favourites")
    Call<Void> deleteAllUserFavourites(@Path("userId") String userId);

    @DELETE("/api/default_user/{userId}/delete_user")
    Call<Void> deleteUser(@Path("userId") String userId);

    @POST("/api/default_user/{userId}/add_event/{eventId}")
    Call<Void> addEventToFavourites(@Path("userId") String userId, @Path("eventId") String eventId);

    @DELETE("/api/default_user/{userId}/delete_event_favourites/{eventId}")
    Call<Void> removeEventFromFavourites(@Path("userId") String userId, @Path("eventId") String eventId);

    @POST("/api/default_user/{userId}/add_spot/{eventId}")
    Call<Void> addSpotToFavourites(@Path("userId") String userId, @Path("spotId") String spotId);

    @DELETE("/api/default_user/{userId}/delete_spot_favourites/{spotId}")
    Call<Void> removeSpotFromFavourites(@Path("userId") String userId, @Path("spotId") String spotId);

    @GET("/api/default_user/{userId}/events/{eventId}/is_favorite")
    Call<Boolean> isEventInFavorites(@Path("userId") String userId, @Path("eventId") String eventId);

    @GET("/api/default_user/{userId}/spots/{spotId}/is_favorite")
    Call<Boolean> isSpotInFavorites(@Path("userId") String userId, @Path("spotId") String spotId);

}
