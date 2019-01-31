package com.example.weather;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

public class MainViewModel extends ViewModel {
    private PlacesRepository mRepository;
    private LiveData<List<Place>> mPlaces;
    private LiveData<List<Place>> mUpdatedPlaces;

    public MainViewModel(Context context) {
        mRepository = new PlacesRepository(context);
        mPlaces = mRepository.getPlaces();
    }

    public LiveData<List<Place>> getPlaces () {
        return mPlaces;
    }

    public LiveData<List<Place>> getUpdatedPlaces () {
        return mPlaces;
    }

    public void insert (Place place) {
        mRepository.insert(place);
    }

    public void delete (Place place) {
        mRepository.delete(place);}
}
