package com.panapptix.weather;

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
/*
    public LiveData<List<Place>> getUpdatedPlaces() {

        for (int i=0;i <= mPlaces.getValue().size();i++) {
            updateWeather(mPlaces.getValue().get(i).toString());
            mPlaces = new MutableLiveData<>();
            ((MutableLiveData<List<Place>>) mPlaces).setValue(updatePlaces());
        }
        return mPlaces;
    }

    private Place updateWeather(String place) {
        WeatherApi service = WeatherApi.retrofit.create(WeatherApi.class);
        Result weatherResult = null;
        Place newPlace = null;

        String key = service.API_KEY;
        final String city = place;
        final String units = "metric";

        Call<Result> call = service.getWeatherInPlace(place, key, units);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();

                    ArrayList theWeather = new ArrayList();
                    theWeather.addAll(result.getWeather());
                    Weather weatherResult = (Weather) theWeather.get(0);
                    String weather = weatherResult.getMain();

                    String image = "http://openweathermap.org/img/w/"+weatherResult.getIcon()+".png";

                    Main mainResult = result.getMain();
                    Float floatTemp = mainResult.getTemp();
                    int temp = Math.round(floatTemp);

                    String theCity = result.getName();

                    Place newPlace = new Place(theCity, temp, weather, image, "");
                }
                else return;
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }
        });
        return newPlace;
    }

    private List updatePlaces(){
        List<Place> placesList = null;

        for (int i=0;i <= mPlaces.getValue().size();i++){
            Place place = updateWeather(mPlaces.getValue().get(i).getName());
            placesList.add(place);
        }
        return placesList;
    }
*/
}
