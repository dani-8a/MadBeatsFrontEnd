package com.example.madbeatsfrontend.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.FavouriteEventsAdapter;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.viewModel.FavouritesViewModel;

import java.util.List;

public class FavouriteEventsFragment extends Fragment {

    private static final String SHARED_PREFS_NAME = "UserData";
    private static final String KEY_USER_ID = "idUser";

    private FavouritesViewModel favouritesViewModel;
    private RecyclerView recyclerView;
    private FavouriteEventsAdapter favouriteEventsAdapter;

    public FavouriteEventsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouritesViewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        int layoutId = isLoggedIn ? R.layout.fragment_login_events : R.layout.fragment_empty_logout_events;
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(KEY_USER_ID, null);

        if (userId != null) {
            recyclerView = view.findViewById(R.id.eventFavouritesRV);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            favouriteEventsAdapter = new FavouriteEventsAdapter(new FavouriteEventsAdapter.OnItemClickListener() {
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
            recyclerView.setAdapter(favouriteEventsAdapter);

            favouritesViewModel.getFavouriteEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
                @Override
                public void onChanged(List<Event> events) {
                    if (events != null && !events.isEmpty()) {
                        favouriteEventsAdapter.setEvents(events);
                    }
                }
            });

            favouritesViewModel.loadUserFavouriteEvents(userId);
        }
    }
}
