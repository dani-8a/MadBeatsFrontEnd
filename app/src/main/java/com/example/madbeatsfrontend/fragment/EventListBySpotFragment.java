package com.example.madbeatsfrontend.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.EventListBySpotAdapter;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventListBySpotFragment extends Fragment {

    private EventsSpotsViewModel eventsSpotsViewModel;
    Button backButton;
    ToggleButton favButton;
    TextView txtSpotName, txtSpotAddress;
    RecyclerView eventListRV;
    EventListBySpotAdapter eventListBySpotAdapter;

    public EventListBySpotFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list_by_spot, container, false);

        eventListRV = view.findViewById(R.id.eventListRV);
        txtSpotName = view.findViewById(R.id.txtSpotName);
        txtSpotAddress = view.findViewById(R.id.txtSpotAddress);
        backButton = view.findViewById(R.id.button_back);
        favButton = view.findViewById(R.id.button_fav);

        LinearLayoutManager eventLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        eventListRV.setLayoutManager(eventLayoutManager);

        eventListBySpotAdapter = new EventListBySpotAdapter(new ArrayList<>(), new EventListBySpotAdapter.OnItemClickListener() {
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
        eventListRV.setAdapter(eventListBySpotAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        Bundle arguments = getArguments();
        if (arguments != null) {
            String spotId = arguments.getString("spotId");
            if (spotId != null) {
                // Cargar información del spot y eventos asociados
                eventsSpotsViewModel.loadSpotWithEvents(spotId);
                observeSpotWithEventsLiveData();
            } else {
                txtSpotName.setText("N/A");
                txtSpotAddress.setText("N/A");
            }
        }

        return view;
    }

    private void observeSpotWithEventsLiveData() {
        eventsSpotsViewModel.getSpotWithEventsLiveData().observe(getViewLifecycleOwner(), new Observer<SpotWithEventResponse>() {
            @Override
            public void onChanged(SpotWithEventResponse spotWithEventResponse) {
                if (spotWithEventResponse != null) {
                    // Mostrar la información del spot
                    txtSpotName.setText(spotWithEventResponse.getNameSpot());
                    txtSpotAddress.setText(spotWithEventResponse.getAddressSpot());

                    // Actualizar la lista de eventos asociados al spot
                    List<Event> events = spotWithEventResponse.getEvents();
                    eventListBySpotAdapter.updateEventList(events);
                } else {
                    txtSpotName.setText("N/A");
                    txtSpotAddress.setText("N/A");
                    // Limpiar la lista de eventos
                    eventListBySpotAdapter.clearEventList();
                }
            }
        });
    }
}
