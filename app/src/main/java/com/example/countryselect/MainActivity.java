package com.example.countryselect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.countryselect.data.CountryData;
import com.example.countryselect.data.CountryDataResponse;
import com.example.countryselect.data.WeatherData;
import com.example.countryselect.databinding.ActivityMainBinding;
import com.example.countryselect.databinding.LayoutBottomSheetCountrySelectBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CountrySelectAdapter.CountrySelectAdapterListener {

    private ActivityMainBinding binding;
    private ApiService service;
    private boolean BOTTOMSHEET_CAN_CLOSE = true;
    private CountryData selectedCountryData = null;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //Not the right place to call. Use dagger to inject them on application level
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.printful.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        setContentView(binding.getRoot());
        binding.constraintLayout.setOnClickListener(new SingleOnClickListener(){
            @Override
            public void onSingleClick(View v) {
                Log.e("sd", "sd");
                callCountryData();
            }
        } );
        setSelectedCountry();

    }

    private void setSelectedCountry() {
        String countryJson = PreferenceHelper.getSharedPreferenceString(this, "SELECTED_COUNTRY", null);
        if (!TextUtils.isEmpty(countryJson)) {
            selectedCountryData = new Gson().fromJson(countryJson, CountryData.class);
            setSelectedCountryData(selectedCountryData);
        } else {
            BOTTOMSHEET_CAN_CLOSE = false;
            callCountryData();
        }
    }

    private void callCountryData() {
        LayoutBottomSheetCountrySelectBinding binding = LayoutBottomSheetCountrySelectBinding.inflate(getLayoutInflater(), null, false);

        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setCancelable(BOTTOMSHEET_CAN_CLOSE);
        bottomSheetDialog.setContentView(binding.getRoot());


        Call<CountryDataResponse> call = service.getCountries();
        call.enqueue(new Callback<CountryDataResponse>() {
            @Override
            public void onResponse(Call<CountryDataResponse> call, Response<CountryDataResponse> response) {
                final List<CountryData> countryDataList = response.body() != null ? response.body().getResult() : new ArrayList<>();
                //List<CountryData> countryDataList = Arrays.asList(new CountryData("In", "India"),new CountryData("Bn", "Berlin") );
                if (selectedCountryData != null) {
                    for (CountryData countryData : countryDataList) {
                        if (countryData.getCode().equals(selectedCountryData.getCode())) {
                            countryData.setSelected(true);
                        }
                    }
                }
                binding.rvCountrySelect.setAdapter(new CountrySelectAdapter(countryDataList, MainActivity.this));
                bottomSheetDialog.show();

            }

            @Override
            public void onFailure(Call<CountryDataResponse> call, Throwable t) {
                Log.e("error", t.getStackTrace().toString());
            }
        });
    }


    private void setSelectedCountryData(CountryData countryData) {
        Glide
                .with(binding.getRoot().getContext())
                .load("https://www.countryflags.io/" + countryData.getCode() + "/flat/24.png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivCountry);
        binding.tvCountry.setText(countryData.getName());


        setWeatherData(countryData.getCode());
    }


    private void setWeatherData(String countryCode){
        Call<WeatherData> call = service.getWeatherData("https://countrycode.org/api/weather/conditions?country=" + countryCode);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                WeatherData data = response.body();
                binding.llWeather.mainWeather.setText(data.temp + " " + data.tempUnits );
                binding.llWeather.conditions.setText(data.conditions);
                binding.llWeather.secondaryWeather.setText(data.high + "/" + data.low + " " +data.tempUnits );
                Glide
                        .with(binding.getRoot().getContext())
                        .load(data.imageUrl)
                        .centerCrop()
                        .into(binding.llWeather.ivImage);
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(CountryData countryData) {
        selectedCountryData = countryData;
        String json = new Gson().toJson(countryData);
        PreferenceHelper.setSharedPreferenceString(this, "SELECTED_COUNTRY", json);
        setSelectedCountryData(countryData);
        if(bottomSheetDialog != null) bottomSheetDialog.hide();
    }
}