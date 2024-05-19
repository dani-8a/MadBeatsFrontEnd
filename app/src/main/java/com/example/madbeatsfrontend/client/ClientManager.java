package com.example.madbeatsfrontend.client;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientManager {
//https://madbeats-backend-93a77b2b6978.herokuapp.com
    private static final String BASE_URL = "https://madbeats-backend-93a77b2b6978.herokuapp.com"; // CASA (Madrid): 192.168.1.116 // iPhone: 172.24.64.1 // CASA (zgz): 172.30.96.1
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit;
    }

    public static APIService getApiService() {
        return getRetrofitInstance().create(APIService.class);
    }

}
