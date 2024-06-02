package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.client.GeocodingService;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;
import com.example.madbeatsfrontend.viewModel.FavouritesViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EventInfoFragment extends Fragment implements OnMapReadyCallback {
    private EventsSpotsViewModel eventsSpotsViewModel;
    private FavouritesViewModel favouritesViewModel;
    private GeocodingService geocodingService;
    private Button backButton, addButton;
    private TextView txtNameEvent, txtNameSpot, txtAddressSpot, txtArtists, txtDate, txtSchedule, txtPrice,
            txtAge, txtMusicCategory, txtMusicGenre, txtURL, txtDressCode;
    private GoogleMap map;

    public EventInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
        favouritesViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
        geocodingService = new GeocodingService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        backButton = view.findViewById(R.id.button_back);
        addButton = view.findViewById(R.id.button_add);
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
                    txtNameEvent.setText("Event: " + (event.getNameEvent() != null ? event.getNameEvent() : "N/A"));
                    txtNameSpot.setText("Spot: " + (event.getSpot().getNameSpot() != null ? event.getSpot().getNameSpot() : "N/A"));
                    txtAddressSpot.setText("Address: " + (event.getSpot().getAddressSpot() != null ? event.getSpot().getAddressSpot() : "N/A"));
                    txtArtists.setText("Artists: " + (event.getArtists() != null ? event.getArtists() : "N/A"));
                    txtDate.setText("Date: " + (event.getDate() != null ? event.getDate() : "N/A"));
                    txtSchedule.setText("Schedule: " + (event.getSchedule() != null ? event.getSchedule() : "N/A"));
                    txtPrice.setText("Price: " + event.getPrice() + "€");
                    txtAge.setText("Minimum age: " + event.getMinimumAge());
                    txtMusicCategory.setText("Music category: " + (event.getMusicCategory() != null ? event.getMusicCategory() : "N/A"));
                    txtMusicGenre.setText("Music genres: " + (event.getMusicGenres() != null ? event.getMusicGenres() : "N/A"));
                    txtURL.setText("URL: " + (event.getUrlEvent() != null ? event.getUrlEvent() : "N/A"));
                    txtDressCode.setText("Dress code: " + (event.getDressCode() != null ? event.getDressCode() : "N/A"));

                    // Geocodificar la dirección del spot y actualizar el marcador en el mapa
                    List<String> addresses = Collections.singletonList(event.getSpot().getAddressSpot());
                    geocodingService.geocodeAddressesToLatLng(addresses, new GeocodingService.GeocodeCallback() {
                        @Override
                        public void onGeocodeSuccess(Map<String, LatLng> coordinatesMap) {
                            LatLng spotLatLng = coordinatesMap.get(event.getSpot().getAddressSpot());
                            if (spotLatLng != null && map != null) {
                                map.clear();
                                map.addMarker(new MarkerOptions().position(spotLatLng).title(event.getSpot().getNameSpot()));
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(spotLatLng, 15f);
                                map.moveCamera(cameraUpdate);
                            }
                        }

                        @Override
                        public void onGeocodeFailure() {
                            Toast.makeText(getContext(), "Failed to geocode address", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("EventInfoFragment", "Event is null");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el usuario está autenticado
                String userId = getUserIdFromSharedPreferences();
                if (userId == null) {
                    // Si el usuario no está autenticado, mostrar el diálogo de inicio de sesión
                    showLoginAlertDialog();
                } else {
                    // Si el usuario está autenticado, llamar al método para agregar el evento a favoritos
                    Bundle arguments = getArguments();
                    if (arguments != null) {
                        String eventId = arguments.getString("eventId");
                        if (eventId != null) {
                            // Llamar al método para agregar el evento a favoritos
                            favouritesViewModel.addEventToFavourites(userId, eventId);
                            Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("EventInfoFragment", "Event ID is null");
                        }
                    } else {
                        Log.e("EventInfoFragment", "Arguments are null");
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng defaultLocation = new LatLng(40.4168, -3.7038);
        map.addMarker(new MarkerOptions().position(defaultLocation).title("Madrid"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f);
        map.moveCamera(cameraUpdate);
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("idUser", null);
    }

    private void showLoginAlertDialog() {
        new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Login Required")
                .setMessage("Please, log in to like events & spots")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
