package com.example.countryselect;

import com.example.countryselect.data.CountryDataResponse;
import com.example.countryselect.data.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    @GET("/countries")
    Call<CountryDataResponse> getCountries();

    @GET
    Call<WeatherData> getWeatherData(@Url String url);

}
