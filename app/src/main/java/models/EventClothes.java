package models;


public class EventClothes {
    public double moy;
    public boolean isRaining;

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
