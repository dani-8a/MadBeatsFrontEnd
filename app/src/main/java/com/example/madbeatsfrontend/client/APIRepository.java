package com.example.madbeatsfrontend.client;

import com.example.madbeatsfrontend.entity.DefaultUser;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class APIRepository {
    private APIService apiService = APIConnection.getApiService();

    public void getAllEvents(Callback<List<Event>> callback) {
        apiService.getAllEvents().enqueue(callback);
    }

    public void getAllSpots(Callback<List<Spot>> callback) {
        apiService.getAllSpots().enqueue(callback);
    }

    public void getEventInfo(String eventId, Callback<Event> callback) {
        apiService.getEventInfo(eventId).enqueue(callback);
    }

    public void getSpotWithEvents(String spotId, Callback<SpotWithEventResponse> callback){
        apiService.getSpotWithEvents(spotId).enqueue(callback);
    }

    public void getSpotsByMusicCategory(String musicCategory, Callback<List<Spot>> callback){
        apiService.getSpotsByMusicCategory(musicCategory).enqueue(callback);
    }

    public void getSpotsByEventDate(int day, int month, int year, Callback<List<Spot>> callback) {
        apiService.getSpotsByEventDate(day, month, year).enqueue(callback);
    }

    public void loginUser(DefaultUser user, Callback<DefaultUser> callback) {
        apiService.loginUser(user).enqueue(callback);
    }

    public void registerUser(DefaultUser user, Callback<ResponseBody> callback) {
        apiService.registerUser(user).enqueue(callback);
    }

    public void getUserFavouriteEvents(String userId, Callback<List<Event>> callback){
        apiService.getUserFavouriteEvents(userId).enqueue(callback);
    }

    public void getUserFavouriteSpots(String userId, Callback<List<Spot>> callback){
        apiService.getUserFavouriteSpots(userId).enqueue(callback);
    }

    public void deleteAllUserFavourites(String userId, Callback<Void> callback) {
        apiService.deleteAllUserFavourites(userId).enqueue(callback);
    }

    public void deleteUser(String userId, Callback<Void> callback) {
        apiService.deleteUser(userId).enqueue(callback);
    }

    public void addEventToFavourites(String userId, String eventId, Callback<Void> callback) {
        apiService.addEventToFavourites(userId, eventId).enqueue(callback);
    }

    public void removeEventFromFavourites(String userId, String eventId, Callback<Void> callback) {
        apiService.removeEventFromFavourites(userId, eventId).enqueue(callback);
    }

    public void addSpotToFavourites(String userId, String spotId, Callback<Void> callback) {
        apiService.addSpotToFavourites(userId, spotId).enqueue(callback);
    }

    public void removeSpotFromFavourites(String userId, String spotId, Callback<Void> callback) {
        apiService.removeSpotFromFavourites(userId, spotId).enqueue(callback);
    }

    public void isEventInFavorites(String userId, String eventId, Callback<Boolean> callback) {
        apiService.isEventInFavorites(userId, eventId).enqueue(callback);
    }

    public void isSpotInFavorites(String userId, String spotId, Callback<Boolean> callback) {
        apiService.isSpotInFavorites(userId, spotId).enqueue(callback);
    }


}
