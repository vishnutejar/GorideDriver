package com.tranxit.enterprise.ui.activity.main;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.ApiInterface;
import com.tranxit.enterprise.data.network.model.OTPResponse;
import com.tranxit.enterprise.data.network.model.SettingsResponse;
import com.tranxit.enterprise.data.network.model.TripResponse;
import com.tranxit.enterprise.data.network.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.tranxit.enterprise.ui.activity.regsiter.RegisterPresenter;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter<V extends MainIView> extends BasePresenter<V> implements MainIPresenter<V> {

    @Override
    public void getProfile() {
        Observable modelObservable = APIClient.getAPIClient().getProfile();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((User) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }

    @Override
    public void logout(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().logout(obj);

        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccessLogout(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));


    }

    @Override
    public void getTrip(HashMap<String, Object> params) {
        Observable modelObservable = APIClient.getAPIClient().getTrip(params);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((TripResponse) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }

    @Override
    public void providerAvailable(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().providerAvailable(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccessProviderAvailable(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }

    @Override
    public void sendFCM(JsonObject jsonObject) {

        ApiInterface FcmAPI = APIClient.getFcmRetrofit().create(ApiInterface.class);
        Observable modelObservable = FcmAPI.sendFcm(jsonObject);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccessFCM(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));


    }

    @Override
    public void getTripLocationUpdate(HashMap<String, Object> params) {

        Observable modelObservable = APIClient.getAPIClient().getTrip(params);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccessLocationUpdate((TripResponse) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }

    @Override
    public void locationUpdateServer(LatLng latLng) {

        Observable modelObservable = APIClient.getAPIClient().serverLocationUpdate(latLng.latitude, latLng.longitude);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                               @Override
                               public void accept(Object trendsResponse) throws Exception {
                                   MainPresenter.this.getMvpView().onSuccessServerLocationUpdate(trendsResponse);
                               }
                           },
                        new Consumer() {
                            @Override
                            public void accept(Object throwable) throws Exception {
                               //MainPresenter.this.getMvpView().onError((Throwable) throwable);
                            }
                        });

    }

    @Override
    public void instantRideEstimateFare(HashMap<String, Object> obj) {
        getMvpView().showLoading();
        Observable modelObservable = APIClient.getAPIClient().instantRideEstimateFare(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> {
                            getMvpView().hideLoading();
                            MainPresenter.this.getMvpView().onSuccessInstant((OTPResponse) trendsResponse);
                        },
                        (Consumer) throwable -> {
                            getMvpView().hideLoading();
                            MainPresenter.this.getMvpView().onError((Throwable) throwable);
                        });

    }


    @Override
    public void instantRideSendRequest(HashMap<String, Object> obj) {


        Observable modelObservable = APIClient.getAPIClient().instantRideSendRequest(obj);
        getMvpView().showLoading();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> {
                            getMvpView().hideLoading();
                            MainPresenter.this.getMvpView().onSuccessInstantNow((Object) trendsResponse);
                        },
                        throwable -> {
                            getMvpView().hideLoading();
                            MainPresenter.this.getMvpView().onError((Throwable) throwable);
                        });

    }

    @Override
    public void getSettings() {
        Observable modelObservable = APIClient.getAPIClient().getSettings();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    MainPresenter.this.getMvpView().onSuccess((SettingsResponse) response);
                }, (Consumer) throwable -> {
                    MainPresenter.this.getMvpView().onError((Throwable) throwable);
                });
    }
}
