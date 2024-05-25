package com.example.madbeatsfrontend.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.CustomInfoWindowAdapter;
import com.example.madbeatsfrontend.client.GeocodingService;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.interfaces.OnSpotsFilteredListener;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnSpotsFilteredListener {
    private EventsSpotsViewModel eventsSpotsViewModel;
    private GoogleMap map;
    private Button buttonDate, buttonMusicCat, buttonResetFilters;
    private boolean isCalendarOpen = false;
    private boolean isMusicCategoriesOpen = false;
    private GeocodingService geocodingService = new GeocodingService();
    private boolean musicCategoryFilterApplied = false;
    private String selectedMusicCategory = "";
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonDate = view.findViewById(R.id.buttonDate);
        buttonMusicCat = view.findViewById(R.id.buttonMusicCat);
        buttonResetFilters = view.findViewById(R.id.buttonResetFilters);
        CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(requireContext());

        if (map != null) {
            map.setInfoWindowAdapter(infoWindowAdapter);
        }

        buttonDate.setOnClickListener(v -> {
            Log.d("SearchFragment", "buttonDate onClick");
            if (isMusicCategoriesOpen) {
                closeMusicCategories();
            }
            openCalendarFragment();
        });

        buttonMusicCat.setOnClickListener(v -> {
            if (isCalendarOpen) {
                closeCalendarFragment();
            }
            openMusicCategories();
        });

        buttonResetFilters.setOnClickListener(v -> {
            if (map != null) {
                map.clear();
            }
            selectedMusicCategory = "";
            selectedDay = 0;
            selectedMonth = 0;
            selectedYear = 0;
            musicCategoryFilterApplied = false;
            eventsSpotsViewModel.loadSpots();
            Toast.makeText(getContext(), "Filters Removed", Toast.LENGTH_SHORT).show();
        });

        eventsSpotsViewModel.getSpotsByEventMusicCategory().observe(getViewLifecycleOwner(), this::onSpotsFiltered);
        eventsSpotsViewModel.getSpotsByEventDateLiveData().observe(getViewLifecycleOwner(), this::onSpotsFiltered);

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

        // Verificar y aplicar los filtros actuales
        if (musicCategoryFilterApplied) {
            eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedMusicCategory);
        } else if (selectedDay != 0 && selectedMonth != 0 && selectedYear != 0) {
            eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
        } else {
            eventsSpotsViewModel.getSpotsLiveData().observe(getViewLifecycleOwner(), this::addMarkersToMap);
        }
    }

    private void addMarkersToMap(List<Spot> spots) {
        if (map != null && spots != null) {
            map.clear();
            List<String> addresses = new ArrayList<>();

            for (Spot spot : spots) {
                addresses.add(spot.getAddressSpot());
            }
            geocodingService.geocodeAddressesToLatLng(addresses, new GeocodingService.GeocodeCallback() {
                @Override
                public void onGeocodeSuccess(Map<String, LatLng> coordinatesMap) {
                    // Este código ya se ejecuta en el hilo principal
                    for (Spot spot : spots) {
                        LatLng coordinates = coordinatesMap.get(spot.getAddressSpot());
                        if (coordinates != null) {
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(coordinates)
                                    .title(spot.getNameSpot())
                                    .snippet(spot.getIdSpot()));
                        } else {
                            Log.e("SearchFragment", "Couldn't get the coordinates for: " + spot.getAddressSpot());
                        }
                    }

                    if (getContext() != null) {
                        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
                        map.setOnInfoWindowClickListener(marker -> openEventListBySpotFragment(marker.getSnippet()));
                    } else {
                        Log.e("SearchFragment", "Context is null when setting CustomInfoWindowAdapter");
                    }
                }

                @Override
                public void onGeocodeFailure() {
                    Log.e("SearchFragment", "Fallo en la geocodificación");
                }
            });
        }
    }

    private void openEventListBySpotFragment(String spotId) {
        EventListBySpotFragment eventListBySpotFragment = new EventListBySpotFragment();
        Bundle args = new Bundle();
        args.putString("spotId", spotId);
        eventListBySpotFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, eventListBySpotFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        // Verificar y aplicar los filtros actuales
        if (musicCategoryFilterApplied) {
            Log.d("OnResume", "Spots filtrados por categoria musical en el mapa");
            eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedMusicCategory);
        } else if (selectedDay != 0 && selectedMonth != 0 && selectedYear != 0) {
            Log.d("OnResume", "Spots filtrados por fecha en el mapa");
            Log.d(TAG, "Selected date: " + selectedDay + "/" + selectedMonth + "/" + selectedYear);
            eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
        } else {
            Log.d("OnResume", "Todos los spots cargados en el maps");
            eventsSpotsViewModel.loadSpots();
        }
    }

    @Override
    public void onSpotsFiltered(List<Spot> spots) {
        addMarkersToMap(spots);
    }

    public void updateMapWithFilteredSpotsByMusicCategory(String selectedCategory) {
        musicCategoryFilterApplied = true;
        selectedMusicCategory = selectedCategory;
        eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedCategory);
    }

    public void updateMapWithFilteredSpotsByDate(int selectedDay, int selectedMonth, int selectedYear) {
        Log.d(TAG, "updateMapWithFilteredSpotsByDate called with date: " + selectedDay + "/" + selectedMonth + "/" + selectedYear);

        musicCategoryFilterApplied = false;
        selectedMusicCategory = "";
        this.selectedDay = selectedDay;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;

        if (map != null) {
            map.clear();
        }

        eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
    }

    private void openCalendarFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment calendarFragment = fragmentManager.findFragmentByTag("CalendarFragment");
        if (calendarFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_container, new CalendarFragment(), "CalendarFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            closeCalendarFragment();
        }
    }

    private void closeCalendarFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment calendarFragment = fragmentManager.findFragmentByTag("CalendarFragment");
        if (calendarFragment != null) {
            fragmentManager.popBackStack();
        }
    }

    private void openMusicCategories() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment musicCategoriesFragment = fragmentManager.findFragmentByTag("MusicCategoriesFragment");
        if (musicCategoriesFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_container, new MusicCategoriesFragment(), "MusicCategoriesFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            closeMusicCategories();
        }
    }

    private void closeMusicCategories() {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment musicCategoriesFragment = fragmentManager.findFragmentByTag("MusicCategoriesFragment");
        if (musicCategoriesFragment != null) {
            fragmentManager.popBackStack();
        }
    }
}

