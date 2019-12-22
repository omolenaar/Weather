package com.panapptix.weather;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "place")
public class Place {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "weather")
    private String weather;
    @ColumnInfo(name = "temperature")
    private Integer temperature;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    @ColumnInfo(name = "windspeed")
    private Float windSpeed;
    @ColumnInfo(name = "winddeg")
    private Float windDeg;
    @ColumnInfo(name = "rainamount")
    private Float rainAmount;
    @ColumnInfo(name = "raindescr")
    private String rainDesciption;

    public Place(String name, String weather, Integer temperature, String imageUrl, Float windSpeed, Float windDeg, Float rainAmount, String rainDesciption) {
        this.name = name;
        this.weather = weather;
        this.temperature = temperature;
        this.description = description;
        this.imageUrl = imageUrl;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.rainAmount = rainAmount;
        this.rainDesciption = rainDesciption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(Float windDeg) {
        this.windDeg = windDeg;
    }

    public Float getRainAmount() {
        return rainAmount;
    }

    public void setRainAmount(Float rainAmount) {
        this.rainAmount = rainAmount;
    }

    public String getRainDesciption() {
        return rainDesciption;
    }

    public void setRainDesciption(String rainDesciption) {
        this.rainDesciption = rainDesciption;
    }
}
