package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.madbeatsfrontend.R;

public class SettingsFragment extends Fragment {
    Button buttonLogin, buttonSignIn, buttonClearFav, buttonMessage, buttonEventManager;
    EditText editMail,editPassword;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        buttonClearFav = view.findViewById(R.id.buttonClearFav);
        buttonMessage = view.findViewById(R.id.buttonMessage);
        buttonEventManager = view.findViewById(R.id.buttonEventManager);
        editMail = view.findViewById(R.id.editMail);
        editPassword = view.findViewById(R.id.editPassword);

        buttonEventManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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
}