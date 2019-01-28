package com.example.weather;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Weather> fromStringToArrayList(String theWeather) {
        if (theWeather == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Weather>>() {}.getType();
        return gson.fromJson(theWeather, listType);
    }

    @TypeConverter
    public static String fromArrayListToString(ArrayList<Weather> theWeather) {
        if (theWeather == null) {
            return (null);
        }
        Gson gson = new Gson();
        return gson.toJson(theWeather);
    }
}
