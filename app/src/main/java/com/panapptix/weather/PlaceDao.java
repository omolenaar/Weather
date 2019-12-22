package com.panapptix.weather;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlace(Place place);

    @Delete
    void delete(Place... place);

    @Query("SELECT * FROM place")
    public LiveData<List<Place>> getAllPlaces();

    @Query("SELECT * FROM Place WHERE name=:name")
    Place getPlace(String name);

}
