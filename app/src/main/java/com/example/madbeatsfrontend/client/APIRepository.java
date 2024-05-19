package com.example.madbeatsfrontend.client;
import com.example.madbeatsfrontend.client.APIService;
import com.example.madbeatsfrontend.client.ClientManager;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRepository {
    private APIService apiService = ClientManager.getApiService();

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

}
