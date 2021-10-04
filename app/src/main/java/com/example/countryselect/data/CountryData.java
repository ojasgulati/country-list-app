package com.example.countryselect.data;


public class CountryData {

    public CountryData(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public CountryData() {
    }

    private String code;
    private String name;
    private boolean isSelected;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
