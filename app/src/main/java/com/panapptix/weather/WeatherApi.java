package com.panapptix.weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    String API_KEY = "8105168418444acb8726dfb4c6276402";

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    /**
     * Create a retrofit client.
     */

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("weather")
    Call<Result> getWeatherInPlace(
            @Query("q") String place,
            @Query("APPID") String API_KEY,
            @Query(("units")) String units
    );

    @GET("weather")
    Call<Result> getWeatherByCoord(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("APPID") String API_KEY,
            @Query(("units")) String units
    );

}
