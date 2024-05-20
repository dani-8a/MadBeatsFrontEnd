package com.example.madbeatsfrontend.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madbeatsfrontend.client.APIRepository;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsSpotsViewModel extends ViewModel {
    private MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Spot>> spotsLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> eventInfoLiveData = new MutableLiveData<>();
    private MutableLiveData<SpotWithEventResponse> spotWithEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Spot>> spotsByEventMusicCategory = new MutableLiveData<>();
    private MutableLiveData<List<Spot>> spotsByEventDateLiveData = new MutableLiveData<>();
    private APIRepository apiRepository = new APIRepository();

    public void loadEvents() {
        apiRepository.getAllEvents(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    List<Event> events = response.body();
                    eventsLiveData.setValue(events);
                    Log.d("EventsSpotsViewModel", "Eventos recibidos: " + events.size());
                } else {
                    // Manejar error de respuesta
                    Log.e("EventsSpotsViewModel", "Error en la respuesta al obtener eventos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                // Manejar error de conexión
                Log.e("EventsSpotsViewModel", "Error de conexión al obtener eventos: " + t.getMessage());
            }
        });
    }

    public void loadSpots() {
        apiRepository.getAllSpots(new Callback<List<Spot>>() {
            @Override
            public void onResponse(Call<List<Spot>> call, Response<List<Spot>> response) {
                if (response.isSuccessful()) {
                    List<Spot> spots = response.body();
                    spotsLiveData.setValue(spots);
                    Log.d("EventsSpotsViewModel", "Spots recibidos: " + spots.size());
                } else {
                    // Manejar error de respuesta
                    Log.e("EventsSpotsViewModel", "Error en la respuesta al obtener spots: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Spot>> call, Throwable t) {
                // Manejar error de conexión
                Log.e("EventsSpotsViewModel", "Error de conexión al obtener spots: " + t.getMessage());
            }
        });
    }

    public void loadEventInfo(String eventId) {
        apiRepository.getEventInfo(eventId, new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    Event event = response.body();
                    // Agregamos un registro para imprimir la respuesta de la API
                    Log.d("API_RESPONSE", "Información del evento recibida: " + event.toString());
                    eventInfoLiveData.setValue(event);
                    //Log.d("EventsSpotsViewModel", "Información del evento recibida: " + event);
                } else {
                    // Manejar error de respuesta
                    Log.e("EventsSpotsViewModel", "Error en la respuesta al obtener información del evento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                // Manejar error de conexión
                Log.e("EventsSpotsViewModel", "Error de conexión al obtener información del evento: " + t.getMessage());
            }
        });
    }

    public void loadSpotWithEvents(String spotId) {
        apiRepository.getSpotWithEvents(spotId, new Callback<SpotWithEventResponse>() {
            @Override
            public void onResponse(Call<SpotWithEventResponse> call, Response<SpotWithEventResponse> response) {
                if (response.isSuccessful()) {
                    SpotWithEventResponse spotWithEventResponse = response.body();
                    spotWithEventsLiveData.setValue(spotWithEventResponse);
                    Log.d("EventsSpotsViewModel", "SpotWithEventResponse recibido: " + spotWithEventResponse);

                    if (spotWithEventResponse != null) {
                        List<Event> events = spotWithEventResponse.getEvents();
                        if (events != null) {
                            eventsLiveData.setValue(events);
                        } else {
                            // Manejar error de respuesta
                            Log.e("EventsSpotsViewModel", "Error: No se encontraron eventos asociados al spot");
                        }
                    } else {
                        // Manejar el caso donde spotWithEventResponse es nulo
                        Log.e("EventsSpotsViewModel", "Error: Respuesta nula al obtener spot con eventos");
                    }
                } else {
                    // Manejar error de respuesta
                    Log.e("EventsSpotsViewModel", "Error en la respuesta al obtener spot con eventos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SpotWithEventResponse> call, Throwable t) {
                // Manejar error de conexión
                Log.e("EventsSpotsViewModel", "Error de conexión al obtener spot con eventos: " + t.getMessage());
            }
        });
    }

    public void loadSpotsByEventMusicCategory(String musicCategory) {
        // Llamar al método del repositorio de la API para obtener los spots por categoría musical
        apiRepository.getSpotsByMusicCategory(musicCategory, new Callback<List<Spot>>() {
            @Override
            public void onResponse(Call<List<Spot>> call, Response<List<Spot>> response) {
                if (response.isSuccessful()) {
                    List<Spot> spots = response.body();
                    spotsByEventMusicCategory.setValue(spots);
                    // Imprimir los spots en el log
                    Log.d("SearchFragment", "Spots filtrados por categoría musical: " + spots);
                } else {
                    // Si la solicitud no fue exitosa, mostrar un mensaje de error en el log
                    Log.e("EventsSpotsViewModel", "Error al obtener los spots por categoría musical");
                }
            }

            @Override
            public void onFailure(Call<List<Spot>> call, Throwable t) {
                // Si la solicitud falla, mostrar un mensaje de error en el log
                Log.e("SearchFragment", "Error al obtener los spots por categoría musical: " + t.getMessage());
            }
        });
    }

    public void loadSpotsByEventDate(int day, int month, int year) {
        apiRepository.getSpotsByEventDate(day, month, year, new Callback<List<Spot>>() {
            @Override
            public void onResponse(Call<List<Spot>> call, Response<List<Spot>> response) {
                if (response.isSuccessful()) {
                    List<Spot> spots = response.body();
                    spotsByEventDateLiveData.setValue(spots);
                    Log.d("EventsSpotsViewModel", "Spots recibidos por fecha de evento: " + spots.size());
                    Log.d("EventsSpotsViewModel", "Spots info: " + spots);
                } else {
                    // Manejar error de respuesta
                    Log.e("EventsSpotsViewModel", "Error en la respuesta al obtener spots por fecha de evento: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Spot>> call, Throwable t) {
                // Manejar error de conexión
                Log.e("EventsSpotsViewModel", "Error de conexión al obtener spots por fecha de evento: " + t.getMessage());
            }
        });
    }

    public LiveData<List<Event>> getEventsLiveData() { return eventsLiveData; }
    public LiveData<List<Spot>> getSpotsLiveData() { return spotsLiveData; }
    public LiveData<Event> getEventInfoLiveData() { return eventInfoLiveData; }
    public LiveData<SpotWithEventResponse> getSpotWithEventsLiveData() { return spotWithEventsLiveData; }
    public LiveData<List<Spot>> getSpotsByEventDateLiveData() { return spotsByEventDateLiveData; }
    public LiveData<List<Spot>> getSpotsByEventMusicCategory(){ return spotsByEventMusicCategory; }

}
