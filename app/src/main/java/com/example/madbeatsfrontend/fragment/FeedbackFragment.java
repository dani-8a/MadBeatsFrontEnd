package com.example.madbeatsfrontend.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.madbeatsfrontend.R;
import com.example.madbeatsfrontend.entity.Feedback;
import com.example.madbeatsfrontend.viewModel.FeedbackViewModel;

import java.util.Arrays;

public class FeedbackFragment extends DialogFragment {

    private FeedbackViewModel feedbackViewModel;

    public FeedbackFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        EditText editMessage = view.findViewById(R.id.editMessage);
        Button buttonSend = view.findViewById(R.id.buttonSend);

        feedbackViewModel.getResponseLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            }
        });

        buttonSend.setOnClickListener(v -> {
            hideKeyboard(v);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = view.findViewById(selectedId);
            String subject = selectedRadioButton.getText().toString();
            String message = editMessage.getText().toString();

            if (!message.isEmpty()) {
                Feedback feedback = new Feedback(null, Arrays.asList(subject), message);
                feedbackViewModel.sendMessageFeedback(feedback);
                dismiss();
                Toast.makeText(getContext(), "Message Sent", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
