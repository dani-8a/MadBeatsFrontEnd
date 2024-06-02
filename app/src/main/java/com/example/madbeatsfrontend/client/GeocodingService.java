package com.example.madbeatsfrontend.client;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeocodingService {
    private MutableLiveData<LatLng> geocodeResultLiveData = new MutableLiveData<>();
    private GeocodeCallback geocodeCallback;
    private Map<String, LatLng> cachedCoordinates = new HashMap<>(); // Cache de coordenadas

    public void geocodeAddressesToLatLng(List<String> addresses, GeocodeCallback callback) {
        this.geocodeCallback = callback;

        // Verificar si ya tenemos las coordenadas cacheadas
        Map<String, LatLng> resultMap = new HashMap<>();
        List<String> addressesToGeocode = new ArrayList<>();
        for (String address : addresses) {
            if (cachedCoordinates.containsKey(address)) {
                resultMap.put(address, cachedCoordinates.get(address));
            } else {
                addressesToGeocode.add(address);
            }
        }

        // Si todas las coordenadas están cacheadas, invocar el callback inmediatamente
        if (addressesToGeocode.isEmpty()) {
            if (geocodeCallback != null) {
                new Handler(Looper.getMainLooper()).post(() -> geocodeCallback.onGeocodeSuccess(resultMap));
            }
            return;
        }

        new Thread(() -> {
            String apiKey = "AIzaSyCtOBcYblWexQHZEA-MZuF4Owuk5zFCxMY";  // Reinsertando la API key

            for (String address : addressesToGeocode) {
                try {
                    String encodedAddress = URLEncoder.encode(address, "UTF-8");
                    URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + apiKey);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Parsear la respuesta JSON para obtener las coordenadas
                        JSONObject jsonObject = new JSONObject(result.toString());
                        if (jsonObject.getString("status").equals("OK")) {
                            JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            LatLng latLng = new LatLng(lat, lng);
                            resultMap.put(address, latLng);
                            cachedCoordinates.put(address, latLng); // Cachear las coordenadas
                        } else {
                            // Manejar errores de la API de Geocoding
                            String errorMessage = jsonObject.getString("error_message");
                            Log.e("Geocode Error", errorMessage);
                            resultMap.put(address, null);
                        }
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    resultMap.put(address, null);
                }
            }

            // Utilizar un Handler para postear los resultados en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                if (geocodeCallback != null) {
                    geocodeCallback.onGeocodeSuccess(resultMap);
                }
            });
        }).start();
    }

    // Interfaz de callback para el resultado de la geocodificación
    public interface GeocodeCallback {
        void onGeocodeSuccess(Map<String, LatLng> coordinatesMap);
        void onGeocodeFailure();
    }

    public MutableLiveData<LatLng> getGeocodeResultLiveData() {
        return geocodeResultLiveData;
    }

    public void clearCache() {
        cachedCoordinates.clear();
    }
}
