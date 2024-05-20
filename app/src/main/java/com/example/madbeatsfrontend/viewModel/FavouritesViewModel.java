package com.example.madbeatsfrontend.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madbeatsfrontend.client.APIRepository;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesViewModel extends ViewModel {
    private final APIRepository apiRepository;
    private final MutableLiveData<List<Event>> favouriteEventsLiveData;
    private final MutableLiveData<List<Spot>> favouriteSpotsLiveData;

    public FavouritesViewModel() {
        apiRepository = new APIRepository();
        favouriteEventsLiveData = new MutableLiveData<>();
        favouriteSpotsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Event>> getFavouriteEvents() {
        return favouriteEventsLiveData;
    }
    public LiveData<List<Spot>> getFavouriteSpots() {
        return favouriteSpotsLiveData;
    }

    public void loadUserFavouriteEvents(String userId) {
        apiRepository.getUserFavouriteEvents(userId, new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favouriteEventsLiveData.setValue(response.body());
                } else {
                    favouriteEventsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                favouriteEventsLiveData.setValue(null);
            }
        });
    }

    public void loadUserFavouriteSpots(String userId) {
        apiRepository.getUserFavouriteSpots(userId, new Callback<List<Spot>>() {
            @Override
            public void onResponse(Call<List<Spot>> call, Response<List<Spot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favouriteSpotsLiveData.setValue(response.body());
                } else {
                    favouriteSpotsLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Spot>> call, Throwable t) {
                favouriteSpotsLiveData.setValue(null);
            }
        });
    }

}
