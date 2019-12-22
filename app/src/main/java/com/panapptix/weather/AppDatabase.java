package com.panapptix.weather;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;


@Database(entities = {Place.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao placeDao();

    private  final static String NAME_DATABASE = "places_db";
    //Static instance

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, NAME_DATABASE).allowMainThreadQueries().build();
        }

        return sInstance;
    }
}
