package com.example.weather;

public class Place {

    private Integer id;
    private String name;
    private Weather weather;
    private Integer temperature;
    private String description;
    private String imageUrl;

    public Place(String name, Integer temperature, Weather weather, String imageUrl, String description) {
        this.name = name;
        this.temperature = temperature;
        this.weather = weather;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
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
}
