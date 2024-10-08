package com.example.madbeatsfrontend.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.adapter.FavouriteEventsAdapter;
import com.example.madbeatsfrontend.adapter.FavouriteSpotsAdapter;
import com.example.madbeatsfrontend.entity.Event;
import com.example.madbeatsfrontend.entity.Spot;
import com.example.madbeatsfrontend.viewModel.FavouritesViewModel;

import java.util.List;

public class FavouriteSpotsFragment extends Fragment {
    private static final String SHARED_PREFS_NAME = "UserData";
    private static final String KEY_USER_ID = "idUser";
    private FavouritesViewModel favouritesViewModel;
    private RecyclerView recyclerView;
    private FavouriteSpotsAdapter favouriteSpotsAdapter;
    private TextView txtEmptyFavSpots;
    private ProgressBar progressBarFavSpots;

    public FavouriteSpotsFragment() {
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

        int layoutId = isLoggedIn ? R.layout.fragment_login_spots : R.layout.fragment_logout_spots;
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(KEY_USER_ID, null);

        if (userId != null) {
            recyclerView = view.findViewById(R.id.spotFavouritesRV);
            txtEmptyFavSpots = view.findViewById(R.id.txtEmptyFavSpots);
            progressBarFavSpots = view.findViewById(R.id.progressBarFavSpots);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            favouriteSpotsAdapter = new FavouriteSpotsAdapter(new FavouriteSpotsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Spot spot) {
                    // Abrir el fragmento EventListBySpot y pasar el spotId como argumento
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    UserEventListBySpotFragment userEventListBySpotFragment = new UserEventListBySpotFragment();

                    // Pasar el ID del spot como argumento al fragmento
                    Bundle bundle = new Bundle();
                    bundle.putString("spotId", spot.getIdSpot());
                    userEventListBySpotFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.frame_container, userEventListBySpotFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            recyclerView.setAdapter(favouriteSpotsAdapter);

            favouritesViewModel.getFavouriteSpots().observe(getViewLifecycleOwner(), new Observer<List<Spot>>() {
                @Override
                public void onChanged(List<Spot> spots) {
                    progressBarFavSpots.setVisibility(View.GONE);
                    if (spots != null && !spots.isEmpty()) {
                        favouriteSpotsAdapter.setSpots(spots);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmptyFavSpots.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        txtEmptyFavSpots.setVisibility(View.VISIBLE);
                    }
                }
            });

            progressBarFavSpots.setVisibility(View.VISIBLE);
            favouritesViewModel.loadUserFavouriteSpots(userId);
        }
    }
}
