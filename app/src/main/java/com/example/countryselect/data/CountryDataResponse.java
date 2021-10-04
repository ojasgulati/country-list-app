package com.example.countryselect.data;

import java.util.List;

public class CountryDataResponse {

    private Integer code;
    private List<CountryData> result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<CountryData> getResult() {
        return result;
    }

    public void setResult(List<CountryData> result) {
        this.result = result;
    }
}
