package com.example.madbeatsfrontend.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.SpotAdapter;
import com.example.madbeatsfrontend.client.GeoRepository;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.interfaces.OnSpotsFilteredListener;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnSpotsFilteredListener {

    private EventsSpotsViewModel eventsSpotsViewModel;
    SpotAdapter spotAdapter;
    RecyclerView spotsRV;
    private GoogleMap map;
    private Button buttonDate, buttonMusicCat, buttonResetFilters;
    private boolean isCalendarOpen = false;
    private boolean isMusicCategoriesOpen = false;
    private GeoRepository geoRepository = new GeoRepository();
    private boolean musicCategoryFilterApplied = false;
    private String selectedMusicCategory = "";
    private  int selectedDate = 0;
    private int selectedDay = 0;
    private int selectedMonth = 0;
    private int selectedYear = 0;

    public SearchFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonDate = view.findViewById(R.id.buttonDate);
        buttonMusicCat = view.findViewById(R.id.buttonMusicCat);
        buttonResetFilters = view.findViewById(R.id.buttonResetFilters);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchFragment", "buttonDate onClick");
                if (isMusicCategoriesOpen) {
                    // Si MusicCategoriesFragment está abierto, ciérralo primero
                    closeMusicCategories();
                }
                // Abrir o cerrar el CalendarFragment según corresponda
                openCalendarFragment();
            }
        });

        buttonMusicCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCalendarOpen) {
                    // Si CalendarFragment está abierto, ciérralo primero
                    closeCalendarFragment();
                }
                // Abrir o cerrar el MusicCategoriesFragment según corresponda
                openMusicCategories();
            }
        });

        buttonResetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.clear();
                }
                eventsSpotsViewModel.loadSpots();
                Toast.makeText(getContext(), "Filters Removed", Toast.LENGTH_SHORT).show();
            }
        });

        eventsSpotsViewModel.getSpotsByEventMusicCategory().observe(getViewLifecycleOwner(), new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                if (spots != null) {
                    onSpotsFiltered(spots);
                }
            }
        });

        eventsSpotsViewModel.getSpotsByEventDateLiveData().observe(getViewLifecycleOwner(), new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                if (spots != null) {
                    onSpotsFiltered(spots);
                }
            }
        });
        // Cargar el mapa
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng madrid = new LatLng(40.4168, -3.7038);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(madrid, 14f);
        map.moveCamera(cameraUpdate);
        map.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    @Override
    public void onResume() {
        super.onResume();
        // Verifica si se ha aplicado un filtro por categoría musical
        if (musicCategoryFilterApplied) {
            Log.d("OnResume", "Spots filtrados por categoria musical en el mapa");
            // Si se ha aplicado un filtro, carga los spots filtrados
            eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedMusicCategory);
        } else if (selectedDay != 0 && selectedMonth != 0 && selectedYear != 0) {
            Log.d("OnResume", "Spots filtrados por fecha en el mapa");
            Log.d(TAG, "Selected date: " + selectedDay + "/" + selectedMonth + "/" + selectedYear);

            // Si se ha seleccionado una fecha, llama al método para cargar los spots filtrados por fecha
            updateMapWithFilteredSpotsByDate(selectedDay, selectedMonth, selectedYear);
        } else {
            Log.d("OnResume", "Todos los spots cargados en el mapa");
            // Si no se ha aplicado ningún filtro, carga todos los spots
            eventsSpotsViewModel.loadSpots();
            eventsSpotsViewModel.getSpotsLiveData().observe(getViewLifecycleOwner(), spots -> {
                showAllSpotsMarkers(spots);
            });
        }
    }


    // Método para mostrar los marcadores en el mapa cuando se cargan todos los Spots
    private void showAllSpotsMarkers(List<Spot> spots) {

        if (map != null && spots != null) {
            List<String> addresses = new ArrayList<>();

            // Obtener las direcciones de los spots y agregarlas a la lista
            for (Spot spot : spots) {
                addresses.add(spot.getAddressSpot());
            }

            // Llamar al método geocodeAddressesToLatLng del GeoRepository
            geoRepository.geocodeAddressesToLatLng(addresses, new GeoRepository.GeocodeCallback() {
                @Override
                public void onGeocodeSuccess(Map<String, LatLng> coordinatesMap) {
                    // Aquí recibes el mapa que asocia cada dirección con sus coordenadas
                    for (Spot spot : spots) {
                        String address = spot.getAddressSpot();
                        LatLng coordinates = coordinatesMap.get(address);
                        if (coordinates != null) {
                            // Aquí puedes mostrar el marcador en el mapa utilizando las coordenadas
                            map.addMarker(new MarkerOptions().position(coordinates).title(spot.getNameSpot()));
                        } else {
                            // Manejar el caso donde no se pudieron obtener las coordenadas para la dirección
                            Log.e("SearchFragment", "No se pudieron obtener las coordenadas para la dirección: " + address);
                        }
                    }
                }

                @Override
                public void onGeocodeFailure() {
                    // Manejar el caso de fallo en la geocodificación
                    Log.e("SearchFragment", "Fallo en la geocodificación");
                }
            });
        }
    }

    @Override
    public void onSpotsFiltered(List<Spot> spots) {
        if (map != null && spots != null) {
            // Borra todos los marcadores existentes en el mapa
            map.clear();

            // Crea una lista para almacenar las direcciones de los spots
            List<String> addresses = new ArrayList<>();

            // Itera sobre los spots para obtener sus direcciones y agregarlas a la lista
            for (Spot spot : spots) {
                addresses.add(spot.getAddressSpot());
            }

            // Llama al método geocodeAddressesToLatLng del GeoRepository para obtener las coordenadas de las direcciones de los spots filtrados
            geoRepository.geocodeAddressesToLatLng(addresses, new GeoRepository.GeocodeCallback() {
                @Override
                public void onGeocodeSuccess(Map<String, LatLng> coordinatesMap) {
                    // Itera sobre los spots filtrados
                    for (Spot spot : spots) {
                        String address = spot.getAddressSpot();
                        LatLng coordinates = coordinatesMap.get(address);
                        if (coordinates != null) {
                            // Agrega nuevos marcadores al mapa con las coordenadas de los spots filtrados
                            map.addMarker(new MarkerOptions().position(coordinates).title(spot.getNameSpot()));
                        } else {
                            // Maneja el caso donde no se pudieron obtener las coordenadas para la dirección
                            Log.e("SearchFragment", "No se pudieron obtener las coordenadas para la dirección: " + address);
                        }
                    }
                }

                @Override
                public void onGeocodeFailure() {
                    // Maneja el caso de fallo en la geocodificación
                    Log.e("SearchFragment", "Fallo en la geocodificación");
                }
            });
        }
    }

    public void updateMapWithFilteredSpotsByMusicCategory(String selectedCategory) {
        eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedCategory);
    }

    public void updateMapWithFilteredSpotsByDate(int selectedDay, int selectedMonth, int selectedYear) {
        Log.d(TAG, "updateMapWithFilteredSpotsByDate called with date: " + selectedDay + "/" + selectedMonth + "/" + selectedYear);

        // Limpiar los marcadores existentes en el mapa
        if (map != null) {
            Log.d( "Check", "map cleared: ");
            map.clear();
        }
        // Llamar al método del ViewModel para obtener los spots filtrados por fecha
        eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
    }

    // Método para abrir el DatePickerFragment
    private void openCalendarFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment calendarFragment = fragmentManager.findFragmentByTag("CalendarFragment");
        Fragment musicCategoriesFragment = new Fragment();
        if (musicCategoriesFragment != null) {
            // Cerrar el fragmento de categorías musicales si está abierto
            closeMusicCategories();
        }
        if (calendarFragment == null) {
            // El fragmento no está abierto, por lo que lo abrimos
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_container, new CalendarFragment(), "CalendarFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            // El fragmento ya está abierto, así que lo cerramos
            closeCalendarFragment();
        }
    }

    private void closeCalendarFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment calendarFragment = fragmentManager.findFragmentByTag("CalendarFragment");
        if (calendarFragment != null) {
            // El fragmento está abierto, por lo que lo cerramos
            fragmentManager.popBackStack();
        }
    }

    private void openMusicCategories() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment musicCategoriesFragment = fragmentManager.findFragmentByTag("MusicCategoriesFragment");
        Fragment calendarFragment = new Fragment();
        if (calendarFragment != null) {
            // Cerrar el fragmento de fechas si está abierto
            closeCalendarFragment();
        }
        if (musicCategoriesFragment == null) {
            // El fragmento no está abierto, por lo que lo abrimos
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_container, new MusicCategoriesFragment(), "MusicCategoriesFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            // El fragmento ya está abierto, así que lo cerramos
            closeMusicCategories();
        }
    }

    private void closeMusicCategories() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment musicCategoriesFragment = fragmentManager.findFragmentByTag("MusicCategoriesFragment");
        if (musicCategoriesFragment != null) {
            // El fragmento está abierto, por lo que lo cerramos
            fragmentManager.popBackStack();
        }
    }
}
