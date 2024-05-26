package com.example.madbeatsfrontend.entity;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {

    private String idEvent;
    private String nameEvent;
    private List<String> artists;
    private String date;
    private String schedule;
    private double price;
    private int minimumAge;
    private String musicCategory;
    private List<String> musicGenres;
    private String urlEvent;
    private String dressCode;
    private Spot spot;

    public Event(String idEvent, String nameEvent, List<String> artists, String date, String schedule, double price, int minimumAge,
                 String musicCategory, List<String> musicGenres, String urlEvent, String dressCode, Spot spot) {
        this.idEvent = idEvent;
        this.nameEvent = nameEvent;
        this.artists = artists;
        this.date = date;
        this.schedule = schedule;
        this.price = price;
        this.minimumAge = minimumAge;
        this.musicCategory = musicCategory;
        this.musicGenres = musicGenres;
        this.urlEvent = urlEvent;
        this.dressCode = dressCode;
        this.spot = spot;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public List<String> getArtists() { return artists; }

    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getMusicCategory() {
        return musicCategory;
    }

    public void setMusicCategory(String musicCategory) {
        this.musicCategory = musicCategory;
    }

    public List<String> getMusicGenres() {
        return musicGenres;
    }

    public void setMusicGenres(List<String> musicGenres) {
        this.musicGenres = musicGenres;
    }

    public String getUrlEvent() {
        return urlEvent;
    }

    public void setUrlEvent(String urlEvent) {
        this.urlEvent = urlEvent;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public String getArtistsAsString() { return String.join(", ", artists); }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent='" + idEvent + '\'' +
                ", nameEvent='" + nameEvent + '\'' +
                ", artists=" + artists +
                ", date='" + date + '\'' +
                ", schedule='" + schedule + '\'' +
                ", price=" + price +
                ", minimumAge=" + minimumAge +
                ", musicCategory='" + musicCategory + '\'' +
                ", musicGenres=" + musicGenres +
                ", urlEvent='" + urlEvent + '\'' +
                ", dressCode='" + dressCode + '\'' +
                ", spot=" + spot +
                '}';
    }
}
