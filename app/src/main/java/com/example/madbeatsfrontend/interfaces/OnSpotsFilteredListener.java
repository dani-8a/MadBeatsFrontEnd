package com.example.madbeatsfrontend.interfaces;

import com.example.madbeatsfrontend.entity.Spot;

import java.util.List;

public interface OnSpotsFilteredListener {
    void onSpotsFiltered(List<Spot> spots);
}