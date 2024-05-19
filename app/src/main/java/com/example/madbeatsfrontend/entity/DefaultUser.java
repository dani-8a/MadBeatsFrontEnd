package com.example.madbeatsfrontend.entity;

import java.util.List;

public class DefaultUser {
    private String idUser;
    private String email;
    private String password;
    private List<Event> favouritesEventList;
    private List<Spot> favouritesSpotList;

    public DefaultUser() {

    }

    public DefaultUser(String idUser, String email, String password, List<Event> favouritesEventList,
                       List<Spot> favouritesSpotList) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.favouritesEventList = favouritesEventList;
        this.favouritesSpotList = favouritesSpotList;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Event> getFavouritesEventList() {
        return favouritesEventList;
    }

    public void setFavouritesEventList(List<Event> favouritesEventList) {
        this.favouritesEventList = favouritesEventList;
    }

    public List<Spot> getFavouritesSpotList() {
        return favouritesSpotList;
    }

    public void setFavouritesSpotList(List<Spot> favouritesSpotList) {
        this.favouritesSpotList = favouritesSpotList;
    }
}
