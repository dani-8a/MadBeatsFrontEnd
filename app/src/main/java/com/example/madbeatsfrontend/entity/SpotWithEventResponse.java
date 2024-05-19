package com.example.madbeatsfrontend.entity;

import java.util.List;

public class SpotWithEventResponse {

    private String idSpot;
    private String nameSpot;
    private String addressSpot;
    private List <Event> events;

    public SpotWithEventResponse(Spot spot, List<Event> events) {
        this.idSpot = spot.getIdSpot();
        this.nameSpot = spot.getNameSpot();
        this.addressSpot = spot.getAddressSpot();
        this.events = events;
    }

    public String getIdSpot() {
        return idSpot;
    }

    public void setIdSpot(String idSpot) {
        this.idSpot = idSpot;
    }

    public String getNameSpot() {
        return nameSpot;
    }

    public void setNameSpot(String nameSpot) {
        this.nameSpot = nameSpot;
    }

    public String getAddressSpot() {
        return addressSpot;
    }

    public void setAddressSpot(String addressSpot) {
        this.addressSpot = addressSpot;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "SpotWithEventResponse [idSpot=" + idSpot + ", nameSpot=" + nameSpot + ", addressSpot=" + addressSpot
                + ", events=" + events + "]";
    }

}
