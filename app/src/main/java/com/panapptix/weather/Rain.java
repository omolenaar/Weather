package com.panapptix.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    @Expose
    private Float _3h;

    public Float get3h() {
        return _3h;
    }

    public void set3h(Float _3h) {
        this._3h = _3h;
    }

    public Float get_3h() {
        return _3h;
    }

    public void set_3h(Float _3h) {
        this._3h = _3h;
    }

    public Rain(Float _3h) {
        this._3h = _3h;
    }

    public String getRainClass() {
        String rainClass = "Rain";
        if (_3h != null) {
            if (_3h < 7.5) rainClass = "Light";
            if (7.5 < _3h && _3h < 30) rainClass = "Moderate";
            if (30 < _3h && _3h < 150) rainClass = "Heavy";
            if (150 < _3h) rainClass = "Violent";
        }
        return rainClass;
        }
    }

