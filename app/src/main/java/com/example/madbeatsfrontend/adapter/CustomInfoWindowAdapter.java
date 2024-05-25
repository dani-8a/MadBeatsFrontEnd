package com.example.madbeatsfrontend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.madbeatsfrontend.R;
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
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Configurar el contenido del InfoWindow
        render(marker, mWindow);
        return mWindow;
    }

    private void render(final Marker marker, View view) {
        TextView textViewName = view.findViewById(R.id.txtSpotName);
        textViewName.setText(marker.getTitle());
    }

}
