package com.example.madbeatsfrontend.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.fragment.EventListBySpotFragment;
import com.example.madbeatsfrontend.fragment.UserEventListBySpotFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // No utilizamos getInfoWindow para personalizar completamente la ventana de información
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Configurar el contenido del InfoWindow
        render(marker, mWindow);
        return mWindow;
    }

    private void render(final Marker marker, View view) {
        // Obtener referencias a los elementos del layout
        TextView textViewName = view.findViewById(R.id.txtSpotName);

        // Configurar el nombre del spot en el TextView
        textViewName.setText(marker.getTitle());
    }

}
