package com.example.madbeatsfrontend.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.LoginViewModel;
import com.example.madbeatsfrontend.entity.DefaultUser;

public class SettingsFragment extends Fragment {
    private Button buttonLogin, buttonLogOut, buttonClearFav, buttonMessage, buttonEventManager, buttonRegister;
    private EditText editMail, editPassword;
    private LoginViewModel viewModel;
    private TextView txtUserState;

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
        buttonLogOut = view.findViewById(R.id.buttonLogOut);
        buttonClearFav = view.findViewById(R.id.buttonClearFav);
        buttonMessage = view.findViewById(R.id.buttonMessage);
        buttonEventManager = view.findViewById(R.id.buttonEventManager);
        editMail = view.findViewById(R.id.editMail);
        editPassword = view.findViewById(R.id.editPassword);
        txtUserState = view.findViewById(R.id.txtUserState);

        // Inicializa el ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observa los cambios en el estado de éxito del login
        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    String email = editMail.getText().toString();
                    String password = editPassword.getText().toString();
                    txtUserState.setText(email + ": You are logged in");
                    txtUserState.setTextColor(Color.GREEN);
                    editMail.setText(email);
                    editPassword.setText(password);
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Verificar si hay datos de usuario guardados en SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("idUser", null);
        String userEmail = sharedPreferences.getString("email", null);
        String userPassword = sharedPreferences.getString("password", null);
        if (userId != null && userEmail != null && userPassword != null) {
            // Actualizar el estado de inicio de sesión si hay datos de usuario guardados
            txtUserState.setText(userEmail + ": You are logged in");
            txtUserState.setTextColor(Color.GREEN);
        }

        // Observa los cambios en el mensaje de error
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

        buttonLogOut.setOnClickListener((v -> {
            viewModel.logoutUser();
            txtUserState.setText("You are not logged in");
            txtUserState.setTextColor(Color.RED);
            Toast.makeText(getContext(), "You logged out", Toast.LENGTH_SHORT).show();
        }));

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
