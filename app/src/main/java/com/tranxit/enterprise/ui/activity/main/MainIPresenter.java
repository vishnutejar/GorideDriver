package com.tranxit.enterprise.ui.activity.main;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface MainIPresenter<V extends MainIView> extends MvpPresenter<V> {

    void getProfile();
    void logout(HashMap<String, Object> obj);
    void getTrip(HashMap<String, Object> params);
    void providerAvailable(HashMap<String, Object> obj);
    void sendFCM(JsonObject jsonObject);
    void getTripLocationUpdate(HashMap<String, Object> params);
    void locationUpdateServer(LatLng latLng);

    void instantRideEstimateFare(HashMap<String, Object> obj);

    void instantRideSendRequest(HashMap<String, Object> obj);
    void getSettings();

}
