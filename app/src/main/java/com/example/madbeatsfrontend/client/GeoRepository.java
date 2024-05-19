package com.example.madbeatsfrontend.client;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoRepository {
    private MutableLiveData<LatLng> geocodeResultLiveData = new MutableLiveData<>();
    private GeocodeCallback geocodeCallback;

    @SuppressLint("StaticFieldLeak")
    public void geocodeAddressesToLatLng(List<String> addresses, GeocodeCallback callback) {
        this.geocodeCallback = callback;
        new AsyncTask<List<String>, Void, Map<String, LatLng>>() {
            @Override
            protected Map<String, LatLng> doInBackground(List<String>... lists) {
                Map<String, LatLng> coordinatesMap = new HashMap<>();
                String apiKey = "AIzaSyCtOBcYblWexQHZEA-MZuF4Owuk5zFCxMY";

                for (String address : lists[0]) {
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
                                coordinatesMap.put(address, latLng);
                            } else {
                                // Manejar errores de la API de Geocoding
                                String errorMessage = jsonObject.getString("error_message");
                                Log.e("Geocode Error", errorMessage);
                                coordinatesMap.put(address, null);
                            }
                        } finally {
                            urlConnection.disconnect();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        coordinatesMap.put(address, null);
                    }
                }
                return coordinatesMap;
            }

            @Override
            protected void onPostExecute(Map<String, LatLng> coordinatesMap) {
                super.onPostExecute(coordinatesMap);
                // Llamar al método de callback con el mapa de coordenadas
                if (geocodeCallback != null) {
                    geocodeCallback.onGeocodeSuccess(coordinatesMap);
                }
            }
        }.execute(addresses);
    }

    // Interfaz de callback para el resultado de la geocodificación
    public interface GeocodeCallback {
        void onGeocodeSuccess(Map<String, LatLng> coordinatesMap);
        void onGeocodeFailure();
    }

    public MutableLiveData<LatLng> getGeocodeResultLiveData() {
        return geocodeResultLiveData;
    }
}
