package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.EventListBySpotAdapter;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.SpotWithEventResponse;
import com.example.madbeatsfrontend.viewModel.EventsSpotsViewModel;
import com.example.madbeatsfrontend.viewModel.FavouritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserEventListBySpotFragment extends Fragment {

    private EventsSpotsViewModel eventsSpotsViewModel;
    private FavouritesViewModel favouritesViewModel;
    Button backButton, deleteButton;
    TextView txtSpotName, txtSpotAddress, txtEmptyEvents;
    RecyclerView eventListRV;
    EventListBySpotAdapter eventListBySpotAdapter;
    ProgressBar progressBarEvents;

    public UserEventListBySpotFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsSpotsViewModel = new ViewModelProvider(requireActivity()).get(EventsSpotsViewModel.class);
        favouritesViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_event_list_by_spot, container, false);

        eventListRV = view.findViewById(R.id.eventListRV);
        txtSpotName = view.findViewById(R.id.txtSpotName);
        txtSpotAddress = view.findViewById(R.id.txtSpotAddress);
        txtEmptyEvents = view.findViewById(R.id.txtEmptyEvents);
        backButton = view.findViewById(R.id.button_back);
        deleteButton = view.findViewById(R.id.button_delete);
        progressBarEvents = view.findViewById(R.id.progressBarEvents);

        LinearLayoutManager eventLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        eventListRV.setLayoutManager(eventLayoutManager);

        eventListBySpotAdapter = new EventListBySpotAdapter(new ArrayList<>(), new EventListBySpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Abrir el EventInfoFragment y pasar el evento seleccionado como argumento
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                EventInfoFragment eventInfoFragment = new EventInfoFragment();

                // Pasar el evento como argumento al fragmento
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
                progressBarEvents.setVisibility(View.VISIBLE);
                eventListRV.setVisibility(View.GONE);
                txtEmptyEvents.setVisibility(View.GONE);
                txtSpotName.setVisibility(View.GONE);
                txtSpotAddress.setVisibility(View.GONE);

                // Cargar información del spot y eventos asociados (SpotWithEventResponse)
                eventsSpotsViewModel.loadSpotWithEvents(spotId);
                observeSpotWithEventsLiveData();
            } else {
                txtSpotName.setText("N/A");
                txtSpotAddress.setText("N/A");
            }
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el usuario está autenticado
                String userId = getUserIdFromSharedPreferences();
                if (userId == null) {
                    showLoginAlertDialog();
                } else {
                    // Si el usuario está autenticado, llamar al método para remover el spot de favoritos
                    Bundle arguments = getArguments();
                    if (arguments != null) {
                        String spotId = arguments.getString("spotId");
                        if (spotId != null) {
                            favouritesViewModel.removeSpotFromFavourites(userId, spotId);
                            Toast.makeText(getContext(), "Removed from Favourites", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("EventInfoFragment", "Spot ID is null");
                        }
                    } else {
                        Log.e("EventInfoFragment", "Arguments are null");
                    }
                }
            }
        });

        return view;
    }

    private void observeSpotWithEventsLiveData() {
        eventsSpotsViewModel.getSpotWithEventsLiveData().observe(getViewLifecycleOwner(), new Observer<SpotWithEventResponse>() {
            @Override
            public void onChanged(SpotWithEventResponse spotWithEventResponse) {
                progressBarEvents.setVisibility(View.GONE); // Ocultar ProgressBar cuando los datos se hayan cargado

                if (spotWithEventResponse != null) {
                    // Mostrar la información del spot
                    txtSpotName.setText(spotWithEventResponse.getNameSpot());
                    txtSpotAddress.setText(spotWithEventResponse.getAddressSpot());

                    // Hacer visibles los TextView
                    txtSpotName.setVisibility(View.VISIBLE);
                    txtSpotAddress.setVisibility(View.VISIBLE);

                    // Actualizar la lista de eventos asociados al spot
                    List<Event> events = spotWithEventResponse.getEvents();
                    if (events != null && !events.isEmpty()) {
                        // Si hay eventos, mostrar el RecyclerView y ocultar el TextView
                        eventListBySpotAdapter.updateEventList(events);
                        eventListRV.setVisibility(View.VISIBLE);
                        txtEmptyEvents.setVisibility(View.GONE);
                    } else {
                        // Si no hay eventos, ocultar el RecyclerView y mostrar el TextView
                        eventListBySpotAdapter.clearEventList();
                        eventListRV.setVisibility(View.GONE);
                        txtEmptyEvents.setVisibility(View.VISIBLE);
                    }
                } else {
                    txtSpotName.setText("N/A");
                    txtSpotAddress.setText("N/A");
                    eventListBySpotAdapter.clearEventList();
                    eventListRV.setVisibility(View.GONE);
                    txtEmptyEvents.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("idUser", null);
    }

    private void showLoginAlertDialog() {
        new AlertDialog.Builder(requireContext())
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
