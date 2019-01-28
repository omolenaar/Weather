package com.example.weather;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlacesRepository {
    private AppDatabase mAppDatabase;
    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mPlaces;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public PlacesRepository(Context context){
        mAppDatabase = AppDatabase.getInstance(context);
        mPlaceDao = mAppDatabase.placeDao();
        mPlaces = mPlaceDao.getAllPlaces();
    }

    public LiveData<List<Place>> getPlaces() {

        return mPlaces;
    }

    public void insert(final Place item) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mPlaceDao.insertPlace(item);
            }
        });
    }

    public void delete(final Place item) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mPlaceDao.delete(item);
            }
        });
    }
}
