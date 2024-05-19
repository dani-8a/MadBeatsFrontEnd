package com.example.madbeatsfrontend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Spot implements Serializable {

    private String idSpot;
    private String nameSpot;
    private String addressSpot;

    public Spot(String idSpot, String nameSpot, String addressSpot) {
        this.idSpot = idSpot;
        this.nameSpot = nameSpot;
        this.addressSpot = addressSpot;
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

    @Override
    public String toString() {
        return "Spot [idSpot=" + idSpot + ", nameSpot=" + nameSpot + ", addressSpot=" + addressSpot + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spot spot = (Spot) o;
        return Objects.equals(idSpot, spot.idSpot) &&
                Objects.equals(nameSpot, spot.nameSpot) &&
                Objects.equals(addressSpot, spot.addressSpot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSpot, nameSpot, addressSpot);
    }
}
