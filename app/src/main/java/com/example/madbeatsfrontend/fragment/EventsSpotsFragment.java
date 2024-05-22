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

public class EventsSpotsFragment extends Fragment {
    RecyclerView spotsRV, eventsRV;
    private EventsSpotsViewModel eventsSpotsViewModel;
    EventAdapter eventAdapter;
    SpotAdapter spotAdapter;
    Button buttonUpdate, buttonAddEvent;

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
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsSpotsViewModel.loadSpots();
                eventsSpotsViewModel.loadEvents();
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
                // Aquí abres el EventInfoFragment y pasas el evento seleccionado como argumento
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EventInfoFragment eventInfoFragment = new EventInfoFragment();

                // Puedes pasar el evento como argumento al fragmento
                Bundle bundle = new Bundle();
                bundle.putString("eventId", event.getIdEvent());
                eventInfoFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_container, eventInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Configurar adaptador de spots con la lista de eventos
        spotAdapter = new SpotAdapter(new ArrayList<>(), new SpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Obtener el spot seleccionado
                Spot spot = spotAdapter.getSpot(position);

                // Obtener el ID del spot seleccionado
                String spotId = spot.getIdSpot();

                // Abrir el fragmento EventListBySpot y pasar el spotId como argumento
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EventListBySpotFragment eventListBySpot = new EventListBySpotFragment();

                // Puedes pasar el ID del spot como argumento al fragmento
                Bundle bundle = new Bundle();
                bundle.putString("spotId", spotId);
                eventListBySpot.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_container, eventListBySpot);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        eventsRV.setAdapter(eventAdapter);
        spotsRV.setAdapter(spotAdapter);

        // Observar los LiveData para eventos y spots
        eventsSpotsViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                // Actualizar la UI con la lista de eventos
                eventAdapter.updateEventList(events);
                Log.d("EventsSpotsFragment", "Eventos recibidos desde ViewModel: " + events.size());
            }
        });

        eventsSpotsViewModel.getSpotsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                // Actualizar la UI con la lista de spots
                spotAdapter.updateSpotList(spots);
                Log.d("EventsSpotsFragment", "Spots recibidos desde ViewModel: " + spots.size());
            }
        });

        return view;
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
        eventsSpotsViewModel.loadSpots();
        eventsSpotsViewModel.loadEvents();
    }
}
