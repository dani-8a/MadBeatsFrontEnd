package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.LoginViewModel;

public class SettingsFragment extends Fragment {
    Button buttonLogin, buttonSignIn, buttonClearFav, buttonMessage, buttonEventManager;
    EditText editMail, editPassword;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonSignIn = view.findViewById(R.id.buttonSignIn);
        buttonClearFav = view.findViewById(R.id.buttonClearFav);
        buttonMessage = view.findViewById(R.id.buttonMessage);
        buttonEventManager = view.findViewById(R.id.buttonEventManager);
        editMail = view.findViewById(R.id.editMail);
        editPassword = view.findViewById(R.id.editPassword);

        // Inicializa el ViewModel
        LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Fallo en el inicio de sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMsg) {
                if (errorMsg != null) {
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configura el OnClickListener para el botón de inicio de sesión
        buttonLogin.setOnClickListener(v -> {
            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();
            viewModel.loginUser(email, password);
        });

        buttonEventManager.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_promoters, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonClose = dialogView.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
    }
}
