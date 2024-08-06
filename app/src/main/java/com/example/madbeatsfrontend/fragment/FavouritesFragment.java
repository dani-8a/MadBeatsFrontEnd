package com.example.madbeatsfrontend.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.example.madbeatsfrontend.R;
import com.google.android.material.tabs.TabLayout;

public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        // Establecer el adaptador para el ViewPager
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        // Conectar el TabLayout al ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    // Adaptador para el ViewPager
    private class PagerAdapter extends FragmentPagerAdapter {

        private static final int NUM_PAGES = 2;

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FavouriteSpotsFragment();
                case 1:
                    return new FavouriteEventsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            // Devuelve el título de la pestaña según la posición
            switch (position) {
                case 0:
                    return "Spots";
                case 1:
                    return "Events";
                default:
                    return null;
            }
        }
    }
}
