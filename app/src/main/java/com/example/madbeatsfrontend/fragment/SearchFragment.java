package com.example.madbeatsfrontend.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
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
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, OnSpotsFilteredListener {
    private EventsSpotsViewModel eventsSpotsViewModel;
    private GoogleMap map;
    private Button buttonDate, buttonMusicCat, buttonResetFilters;
    private GeocodingService geocodingService = new GeocodingService();
    private boolean musicCategoryFilterApplied = false;
    private String selectedMusicCategory = "";
    private int selectedDay = 0;
    private int selectedMonth = 0;
    private int selectedYear = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
        observeViewModel();
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

        buttonDate.setOnClickListener(v -> openCalendarFragment());
        buttonMusicCat.setOnClickListener(v -> openMusicCategoriesFragment());

        buttonResetFilters.setOnClickListener(v -> {
            resetFilters();
            Toast.makeText(getContext(), "Filters Removed", Toast.LENGTH_SHORT).show();
        });

        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    private void observeViewModel() {
        eventsSpotsViewModel.getSpotsLiveData().observe(this, spots -> {
            Log.d("SearchFragment", "Spots loaded: " + spots.size());
            addMarkersToMap(spots);
        });

        eventsSpotsViewModel.getSpotsByEventMusicCategory().observe(this, this::onSpotsFiltered);
        eventsSpotsViewModel.getSpotsByEventDateLiveData().observe(this, this::onSpotsFiltered);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng madrid = new LatLng(40.4168, -3.7038);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(madrid, 14f);
        map.moveCamera(cameraUpdate);
        map.setOnMapClickListener(this);

        loadInitialSpots();
    }

    private void loadInitialSpots() {
        if (musicCategoryFilterApplied) {
            Log.d("SearchFragment", "Loading spots by music category: " + selectedMusicCategory);
            eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedMusicCategory);
        } else if (selectedDay != 0 && selectedMonth != 0 && selectedYear != 0) {
            Log.d("SearchFragment", "Loading spots by date: " + selectedDay + "/" + selectedMonth + "/" + selectedYear);
            eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
        } else {
            Log.d("SearchFragment", "Loading all spots");
            eventsSpotsViewModel.loadSpots();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {}

    @Override
    public void onMapLongClick(LatLng latLng) {}

    private void addMarkersToMap(List<Spot> spots) {
        if (map != null) {
            map.clear();
            if (spots != null) {
                List<String> addresses = new ArrayList<>();
                for (Spot spot : spots) {
                    addresses.add(spot.getAddressSpot());
                }

                geocodingService.geocodeAddressesToLatLng(addresses, new GeocodingService.GeocodeCallback() {
                    @Override
                    public void onGeocodeSuccess(Map<String, LatLng> coordinatesMap) {
                        Log.d("SearchFragment", "Geocoding successful: " + coordinatesMap.size() + " results");
                        for (Spot spot : spots) {
                            LatLng coordinates = coordinatesMap.get(spot.getAddressSpot());
                            if (coordinates != null) {
                                map.addMarker(new MarkerOptions()
                                        .position(coordinates)
                                        .title(spot.getNameSpot())
                                        .snippet(spot.getIdSpot()));
                            }
                        }
                        setupMapInfoWindow();
                    }

                    @Override
                    public void onGeocodeFailure() {
                        Log.e("SearchFragment", "Geocoding failed");
                    }
                });
            }
        }
    }

    private void setupMapInfoWindow() {
        if (getContext() != null) {
            map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));
            map.setOnInfoWindowClickListener(marker -> openEventListBySpotFragment(marker.getSnippet()));
        }
    }

    @Override
    public void onSpotsFiltered(List<Spot> spots) {
        addMarkersToMap(spots);
    }

    public void updateMapWithFilteredSpotsByMusicCategory(String selectedCategory) {
        musicCategoryFilterApplied = true;
        selectedMusicCategory = selectedCategory;
        clearMap();
        eventsSpotsViewModel.loadSpotsByEventMusicCategory(selectedCategory);
    }

    public void updateMapWithFilteredSpotsByDate(int selectedDay, int selectedMonth, int selectedYear) {
        if (this.selectedDay == selectedDay && this.selectedMonth == selectedMonth && this.selectedYear == selectedYear) {
            return;
        }
        musicCategoryFilterApplied = false;
        selectedMusicCategory = "";
        this.selectedDay = selectedDay;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        clearMap();
        eventsSpotsViewModel.loadSpotsByEventDate(selectedDay, selectedMonth, selectedYear);
    }

    private void clearMap() {
        if (map != null) {
            map.clear();
        }
    }

    private void resetFilters() {
        clearMap();
        selectedMusicCategory = "";
        selectedDay = 0;
        selectedMonth = 0;
        selectedYear = 0;
        musicCategoryFilterApplied = false;
        geocodingService.clearCache();
        eventsSpotsViewModel.loadSpots();
    }

    private void openCalendarFragment() {
        CalendarFragment calendarFragment = new CalendarFragment();
        calendarFragment.setTargetFragment(this, 0);
        calendarFragment.show(getParentFragmentManager(), "CalendarFragment");
    }

    private void openMusicCategoriesFragment() {
        MusicCategoriesFragment musicCategoriesFragment = new MusicCategoriesFragment();
        musicCategoriesFragment.setTargetFragment(this, 0);
        musicCategoriesFragment.show(getParentFragmentManager(), "MusicCategoriesFragment");
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
}
