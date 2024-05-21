package com.example.madbeatsfrontend.viewModel;

import android.util.Log;

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
    private final MutableLiveData<Boolean> deleteFavouritesStatus;

    public FavouritesViewModel() {
        apiRepository = new APIRepository();
        favouriteEventsLiveData = new MutableLiveData<>();
        favouriteSpotsLiveData = new MutableLiveData<>();
        deleteFavouritesStatus = new MutableLiveData<>();
    }

    public LiveData<List<Event>> getFavouriteEvents() {
        return favouriteEventsLiveData;
    }
    public LiveData<List<Spot>> getFavouriteSpots() {
        return favouriteSpotsLiveData;
    }
    public LiveData<Boolean> deleteUserFavourites() { return deleteFavouritesStatus; }

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

    public void deleteAllUserFavourites(String userId) {
        Log.d("FavouritesViewModel", "Deleting user favourites for userId: " + userId);
        apiRepository.deleteAllUserFavourites(userId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("FavouritesViewModel", "Successfully deleted user favourites");
                    deleteFavouritesStatus.setValue(true);
                } else {
                    Log.e("FavouritesViewModel", "Failed to delete user favourites. Response code: " + response.code());
                    deleteFavouritesStatus.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FavouritesViewModel", "Failed to delete user favourites. Error: " + t.getMessage());
                deleteFavouritesStatus.setValue(false);
            }
        });
    }


}
