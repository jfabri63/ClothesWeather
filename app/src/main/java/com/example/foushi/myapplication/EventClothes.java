package com.example.foushi.myapplication;


public class EventClothes {
    private double moy;
    private boolean isRaining;

    public double getMoy() {
        return moy;
    }

    public boolean isRaining() {
        return isRaining;
    }

    public EventClothes(double m, boolean p) {
        moy = m;
        isRaining = p;
    }
}
