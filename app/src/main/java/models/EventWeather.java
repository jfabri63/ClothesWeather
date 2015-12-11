package models;

import models.Weather;

public class EventWeather {
    public Weather[] predTemps;

    public EventWeather(Weather[] p) {
        predTemps = p;
    }
}
