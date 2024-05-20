package com.example.madbeatsfrontend.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.madbeatsfrontend.client.APIRepository;
import com.example.madbeatsfrontend.entity.DefaultUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = "LoginViewModel";
    private final APIRepository apiRepository;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<String> errorMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        apiRepository = new APIRepository();
        loginSuccess = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loginUser(String email, String password) {
        DefaultUser user = new DefaultUser();
        user.setEmail(email);
        user.setPassword(password);

        apiRepository.loginUser(user, new Callback<DefaultUser>() {
            @Override
            public void onResponse(Call<DefaultUser> call, Response<DefaultUser> response) {
                if (response.isSuccessful()) {
                    DefaultUser loggedInUser = response.body();
                    if (loggedInUser != null) {
                        // Guardar los datos del usuario en SharedPreferences
                        saveUserData(loggedInUser);
                        loginSuccess.setValue(true);
                        Log.d(TAG, "You are logged in: " + loggedInUser.getEmail());
                    } else {
                        errorMessage.setValue("Respuesta vacía del servidor");
                        Log.e(TAG, "Respuesta vacía del servidor");
                    }
                } else {
                    String errorMsg;
                    switch (response.code()) {
                        case 401:
                            errorMsg = "Unauthorized: Incorrect email or password";
                            break;
                        case 404:
                            errorMsg = "Not Found: User not found";
                            break;
                        default:
                            errorMsg = "Error: " + response.message();
                            break;
                    }
                    errorMessage.setValue(errorMsg);
                    Log.e(TAG, errorMsg);
                }
            }

            @Override
            public void onFailure(Call<DefaultUser> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }

    private void saveUserData(DefaultUser user) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        // Guardar los nuevos datos del usuario que inicia sesión
        editor.putString("idUser", user.getIdUser());
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putString("favouritesEventList", convertListToString(user.getFavouritesEventList()));
        editor.putString("favouritesSpotList", convertListToString(user.getFavouritesSpotList()));
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private String convertListToString(List<?> list) {
        if (list == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object item : list) {
            stringBuilder.append(item.toString()).append(",");
        }
        return stringBuilder.toString();
    }
    public void logoutUser() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

}
