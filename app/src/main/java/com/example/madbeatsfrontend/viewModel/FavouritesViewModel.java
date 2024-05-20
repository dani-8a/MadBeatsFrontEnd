package com.example.madbeatsfrontend.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madbeatsfrontend.client.APIRepository;
import com.example.madbeatsfrontend.entity.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesViewModel extends ViewModel {

    private final APIRepository apiRepository;
    private final MutableLiveData<List<Event>> favouriteEventsLiveData;

    public FavouritesViewModel() {
        apiRepository = new APIRepository();
        favouriteEventsLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Event>> getFavouriteEvents() {
        return favouriteEventsLiveData;
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
}
