package com.example.madbeatsfrontend.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventInfoFragment extends Fragment implements OnMapReadyCallback {
    private EventsSpotsViewModel eventsSpotsViewModel;
    Button backButton;
    ToggleButton favButton;
    TextView txtNameEvent, txtNameSpot, txtAddressSpot, txtArtists, txtDate, txtSchedule, txtPrice,
            txtAge, txtMusicCategory, txtMusicGenre, txtURL, txtDressCode;
    GoogleMap map;

    public EventInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        backButton = view.findViewById(R.id.button_back);
        favButton = view.findViewById(R.id.button_fav);
        txtNameEvent = view.findViewById(R.id.txt_name_event);
        txtNameSpot = view.findViewById(R.id.txt_name_spot);
        txtAddressSpot = view.findViewById(R.id.txt_address_spot);
        txtArtists = view.findViewById(R.id.txt_artists);
        txtDate = view.findViewById(R.id.txt_date);
        txtSchedule = view.findViewById(R.id.txt_schedule);
        txtPrice = view.findViewById(R.id.txt_price);
        txtAge = view.findViewById(R.id.txt_age);
        txtMusicCategory = view.findViewById(R.id.txt_music_category);
        txtMusicGenre = view.findViewById(R.id.txt_music_genre);
        txtURL = view.findViewById(R.id.txt_url);
        txtDressCode = view.findViewById(R.id.txt_dress_code);

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al fragmento anterior (EventsSpotsFragment)
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(); // Regresa al fragmento anterior en la pila de fragmentos
            }
        });

        // Obtener el ID del evento del Bundle de argumentos
        Bundle arguments = getArguments();
        if (arguments != null) {
            String eventId = arguments.getString("eventId");
            if (eventId != null) {
                // Llamar al método loadEventInfo del ViewModel para cargar la información del evento
                eventsSpotsViewModel.loadEventInfo(eventId);
            } else {
                Log.e("EventInfoFragment", "Event ID is null");
            }
        } else {
            Log.e("EventInfoFragment", "Arguments are null");
        }

        // Observar LiveData del ViewModel para recibir la información del evento
        eventsSpotsViewModel.getEventInfoLiveData().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                // Actualizar la interfaz de usuario con la información del evento
                if (event != null) {
                    // Actualizar los TextView con la información del evento
                    txtNameEvent.setText("Event: " + event.getNameEvent());
                    txtNameSpot.setText("Spot: " + event.getSpot().getNameSpot());
                    txtAddressSpot.setText("Address: " + event.getSpot().getAddressSpot());
                    txtArtists.setText("Artists: " + event.getArtists());
                    txtDate.setText("Date: " + event.getDate());
                    txtSchedule.setText("Schedule: " + event.getSchedule());
                    txtPrice.setText("Price: " + event.getPrice() + "€");
                    txtAge.setText("Minimum age: " + event.getMinimumAge());
                    txtMusicCategory.setText("Music category: " + event.getMusicCategory());
                    txtMusicGenre.setText("Music genres: " + event.getMusicGenres());
                    txtURL.setText("URL: " + event.getUrlEvent());
                    txtDressCode.setText("Dress code: " + event.getDressCode());
                } else {
                    Log.e("EventInfoFragment", "Event is null");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar al fragmento anterior (EventsSpotsFragment)
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(); // Regresa al fragmento anterior en la pila de fragmentos
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng madrid = new LatLng(40.4168, -3.7038);
        map.addMarker(new MarkerOptions().position(madrid).title("Madrid"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(madrid, 15f);
        map.moveCamera(cameraUpdate);
    }
}

