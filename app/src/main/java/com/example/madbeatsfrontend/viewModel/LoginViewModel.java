package com.example.madbeatsfrontend.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.madbeatsfrontend.client.APIRepository;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final APIRepository apiRepository;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        apiRepository = new APIRepository();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loginUser(String email, String password) {
        Map<String, String> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("password", password);

        apiRepository.loginUser(fields, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Login", "Inicio de sesión exitoso para " + email);
                    saveUserData(email, password);
                    loginSuccess.setValue(true);
                } else {
                    String errorMsg = "Error: " + response.code() + " " + response.message();
                    errorMessage.setValue(errorMsg);
                    Log.e("Login", errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String errorMsg = "Error: " + t.getMessage();
                errorMessage.setValue(errorMsg);
                Log.e("Login", errorMsg);
            }
        });
    }

    private void saveUserData(String email, String password) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userEmail", email);
        editor.putString("userPassword", password);
        editor.apply();
        logUserData();
    }

    private void logUserData() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("userEmail", "No email found");
        String password = sharedPreferences.getString("userPassword", "No password found");
        Log.d("UserData", "Email: " + email + ", Password: " + password);
    }

}
