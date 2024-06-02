package com.example.madbeatsfrontend.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.UserViewModel;

public class RegisterFragment extends DialogFragment {

    private EditText editTextEmail, editTextPassword;
    private UserViewModel viewModel;
    private OnRegisterClickListener onRegisterClickListener;
    private Button buttonRegister;

    public interface OnRegisterClickListener {
        void onRegisterClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        ImageButton buttonClose = view.findViewById(R.id.buttonClose);

        buttonRegister.setOnClickListener(v -> {
            hideKeyboard(v);
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (isValidEmail(email) && isValidPassword(password)) {
                viewModel.registerUser(email, password);
            }
        });

        buttonClose.setOnClickListener(v -> dismiss());

        viewModel.getRegisterSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    Toast.makeText(getContext(), "User registered", Toast.LENGTH_SHORT).show();
                    if (onRegisterClickListener != null) {
                        onRegisterClickListener.onRegisterClick();
                    }
                    dismiss();
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

        return view;
    }

    public void setOnRegisterClickListener(OnRegisterClickListener listener) {
        onRegisterClickListener = listener;
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
