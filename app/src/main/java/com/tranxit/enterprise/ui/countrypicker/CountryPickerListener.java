package com.tranxit.enterprise.ui.countrypicker;

public interface CountryPickerListener {
    void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID);
}
