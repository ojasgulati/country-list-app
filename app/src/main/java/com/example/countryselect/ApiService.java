package com.example.countryselect;

import com.example.countryselect.data.CountryData;
import com.example.countryselect.data.CountryDataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/countries")
    Call<CountryDataResponse> getCountries();

}
