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
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.FavouritesViewModel;
import com.example.madbeatsfrontend.viewModel.LoginViewModel;

import java.util.concurrent.atomic.AtomicReference;

public class SettingsFragment extends Fragment {
    private Button buttonLogin, buttonLogOut, buttonClearFav, buttonMessage, buttonEventManager, buttonRegister;
    private EditText editMail, editPassword;
    private LoginViewModel viewModel;
    private FavouritesViewModel favouritesViewModel;
    private TextView txtUserState, txtDeleteUser;

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
        buttonRegister = view.findViewById(R.id.buttonRegister);
        editMail = view.findViewById(R.id.editMail);
        editPassword = view.findViewById(R.id.editPassword);
        txtUserState = view.findViewById(R.id.txtUserState);
        txtDeleteUser = view.findViewById(R.id.txtDeleteUser);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        favouritesViewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        if (isUserLoggedIn()) {
            String userEmail = getEmailFromSharedPreferences();
            txtUserState.setText(userEmail + ": You are logged in");
            txtUserState.setTextColor(Color.GREEN);
        } else {
            txtUserState.setText("You are not logged in");
            txtUserState.setTextColor(Color.RED);
        }

        viewModel.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                String email = editMail.getText().toString();
                String password = editPassword.getText().toString();
                txtUserState.setText(email + ": You are logged in");
                txtUserState.setTextColor(Color.GREEN);
                editMail.setText(email);
                editPassword.setText(password);
                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRegisterSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
        favouritesViewModel.deleteUserFavourites().observe(getViewLifecycleOwner(), isDeleted -> {
            if (isDeleted != null) {
                if (isDeleted) {
                    Toast.makeText(getContext(), "All user favourites deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to delete user favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(v -> {
            String email = editMail.getText().toString();
            String password = editPassword.getText().toString();
            viewModel.loginUser(email, password);
        });

        buttonLogOut.setOnClickListener(v -> {
            viewModel.logoutUser();
            txtUserState.setText("You are not logged in");
            txtUserState.setTextColor(Color.RED);
            Toast.makeText(getContext(), "You logged out", Toast.LENGTH_SHORT).show();
        });

        buttonEventManager.setOnClickListener(v -> showDialogPromoters());

        buttonRegister.setOnClickListener(v -> {
            RegisterFragment dialogFragment = new RegisterFragment();
            dialogFragment.setOnRegisterClickListener(() -> dialogFragment.dismiss());
            dialogFragment.show(getChildFragmentManager(), "RegisterDialogFragment");
        });

        buttonClearFav.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                String userId = getUserIdFromSharedPreferences();
                favouritesViewModel.deleteAllUserFavourites(userId);
            } else {
                showDialogClearFavourites();
            }
        });

    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("idUser", null);
        String userEmail = sharedPreferences.getString("email", null);
        String userPassword = sharedPreferences.getString("password", null);

        return userId != null && userEmail != null && userPassword != null;
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("idUser", null);
    }

    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void showDialogPromoters() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_promoters, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonClose = dialogView.findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void showDialogClearFavourites(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_clear_favs, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonClose = dialogView.findViewById(R.id.buttonClose2);
        buttonClose.setOnClickListener(v -> dialog.dismiss());
    }
}
