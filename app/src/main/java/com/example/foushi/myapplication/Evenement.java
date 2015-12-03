package com.example.foushi.myapplication;


public class Evenement {
    private double moy;
    private boolean pluie;

    public double getMoy() {
        return moy;
    }

    public boolean isPluie() {
        return pluie;
    }

    public Evenement(double m, boolean p)
    {
        moy=m;
        pluie=p;
    }
}
