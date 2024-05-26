package com.example.madbeatsfrontend.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madbeatsfrontend.client.APIRepository;
import com.example.madbeatsfrontend.entity.Feedback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackViewModel extends ViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<String> messageFeedbackLiveData;

    public FeedbackViewModel() {
        apiRepository = new APIRepository();
        messageFeedbackLiveData = new MutableLiveData<>();
    }

    public void sendMessageFeedback(Feedback feedback) {
        apiRepository.sendMessageFeedback(feedback, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedbackViewModel", "Feedback submitted successfully");
                    messageFeedbackLiveData.setValue("Feedback submitted successfully");
                } else {
                    Log.d("FeedbackViewModel", "Failed to submit feedback: " + response.message());
                    messageFeedbackLiveData.setValue("Failed to submit feedback: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("FeedbackViewModel", "Error: " + t.getMessage());
                messageFeedbackLiveData.setValue("Error: " + t.getMessage());
            }
        });
    }

    public MutableLiveData<String> getResponseLiveData() {
        return messageFeedbackLiveData;
    }
}
