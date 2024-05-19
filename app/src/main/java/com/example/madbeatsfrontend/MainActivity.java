package com.example.madbeatsfrontend;

import android.os.Bundle;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.madbeatsfrontend.fragment.EventsSpotsFragment;
import com.example.madbeatsfrontend.fragment.FavouritesFragment;
import com.example.madbeatsfrontend.fragment.SearchFragment;
import com.example.madbeatsfrontend.fragment.SettingsFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    SearchFragment searchFragment = new SearchFragment();
    EventsSpotsFragment eventsSpotsFragment = new EventsSpotsFragment();
    FavouritesFragment favouritesFragment = new FavouritesFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(searchFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.searchFragment) {
                loadFragment(searchFragment);
                return true;
            } else if (itemId == R.id.eventsSpotsFragment) {
                loadFragment(eventsSpotsFragment);
                return true;
            } else if (itemId == R.id.favouritesFragment) {
                loadFragment(favouritesFragment);
                return true;
            } else if (itemId == R.id.settingsFragment) {
                loadFragment(settingsFragment);
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}