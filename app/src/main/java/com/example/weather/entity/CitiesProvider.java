package com.example.weather.entity;

import java.util.ArrayList;

public class CitiesProvider {
    private static final ArrayList<City> cities;

    static {
        cities = new ArrayList<>();
        cities.add(new City("Norilsk", 88.192703, 69.352238));
        cities.add(new City("Kazan", 49.13586, 55.7914));
        cities.add(new City("Moscov", 37.617568, 55.75154));
    }

    public static ArrayList<City> getCities() {
        return cities;
    }

    public static void deleteCity(City city) {
        cities.remove(city);
    }

    public static void addCity(City city) {
        cities.add(city);
    }
}
