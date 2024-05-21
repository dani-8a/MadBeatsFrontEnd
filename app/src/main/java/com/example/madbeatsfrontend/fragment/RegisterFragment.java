package com.example.madbeatsfrontend.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.viewModel.LoginViewModel;

public class RegisterFragment extends DialogFragment {

    private EditText editTextEmail, editTextPassword;
    private LoginViewModel viewModel;
    private OnRegisterClickListener mListener;

    public interface OnRegisterClickListener {
        void onRegisterClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        ImageButton buttonClose = view.findViewById(R.id.buttonClose);

        buttonRegister.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                viewModel.registerUser(email, password);
                if (mListener != null) {
                    mListener.onRegisterClick();
                }
                dismiss(); // Cerrar el diálogo después del registro
            } else {
                Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        buttonClose.setOnClickListener(v -> dismiss());

        return view;
    }

    public void setOnRegisterClickListener(OnRegisterClickListener listener) {
        mListener = listener;
    }
}
