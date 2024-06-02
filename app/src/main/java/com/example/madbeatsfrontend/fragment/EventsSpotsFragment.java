package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.EventAdapter;
import com.example.madbeatsfrontend.adapter.SpotAdapter;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.ProgressBar;

public class EventsSpotsFragment extends Fragment {
    RecyclerView spotsRV, eventsRV;
    private EventsSpotsViewModel eventsSpotsViewModel;
    EventAdapter eventAdapter;
    SpotAdapter spotAdapter;
    Button buttonUpdate, buttonAddEvent;
    ProgressBar progressBarEvents, progressBarSpots;

    public EventsSpotsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_spots, container, false);

        eventsRV = view.findViewById(R.id.eventsRV);
        spotsRV = view.findViewById(R.id.spotsRV);
        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonAddEvent = view.findViewById(R.id.buttonAddEvent);
        progressBarEvents = view.findViewById(R.id.progressBarEvents);
        progressBarSpots = view.findViewById(R.id.progressBarSpots);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSpotsAndEvents();
                Toast.makeText(getContext(), "Spots & Events Updated", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // Configurar LayoutManagers para RecyclerViews
        LinearLayoutManager eventLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        eventsRV.setLayoutManager(eventLayoutManager);

        LinearLayoutManager spotLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        spotsRV.setLayoutManager(spotLayoutManager);

        // Inicializar adaptadores
        eventAdapter = new EventAdapter(new ArrayList<>(), new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                openEventInfoFragment(event);
            }
        });

        spotAdapter = new SpotAdapter(new ArrayList<>(), new SpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openEventListBySpotFragment(position);
            }
        });

        eventsRV.setAdapter(eventAdapter);
        spotsRV.setAdapter(spotAdapter);

        // Observar los LiveData para eventos y spots
        eventsSpotsViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventAdapter.updateEventList(events);
                progressBarEvents.setVisibility(View.GONE);
                eventsRV.setVisibility(View.VISIBLE);
            }
        });

        eventsSpotsViewModel.getSpotsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                spotAdapter.updateSpotList(spots);
                progressBarSpots.setVisibility(View.GONE);
                spotsRV.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void loadSpotsAndEvents() {
        progressBarEvents.setVisibility(View.VISIBLE);
        progressBarSpots.setVisibility(View.VISIBLE);
        eventsRV.setVisibility(View.GONE);
        spotsRV.setVisibility(View.GONE);
        eventsSpotsViewModel.loadSpots();
        eventsSpotsViewModel.loadEvents();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_promoters, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonClose = dialogView.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSpotsAndEvents();
    }

    private void openEventInfoFragment(Event event) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventInfoFragment eventInfoFragment = new EventInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putString("eventId", event.getIdEvent());
        eventInfoFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_container, eventInfoFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openEventListBySpotFragment(int position) {
        Spot spot = spotAdapter.getSpot(position);
        String spotId = spot.getIdSpot();

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventListBySpotFragment eventListBySpot = new EventListBySpotFragment();

        Bundle bundle = new Bundle();
        bundle.putString("spotId", spotId);
        eventListBySpot.setArguments(bundle);

        fragmentTransaction.replace(R.id.frame_container, eventListBySpot);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
