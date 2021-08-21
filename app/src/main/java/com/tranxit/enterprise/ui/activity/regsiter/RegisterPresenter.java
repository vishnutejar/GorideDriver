package com.tranxit.enterprise.ui.activity.regsiter;

import android.widget.Toast;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.model.MyOTP;
import com.tranxit.enterprise.data.network.model.SettingsResponse;
import com.tranxit.enterprise.data.network.model.Status;
import com.tranxit.enterprise.data.network.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public class RegisterPresenter<V extends RegisterIView> extends BasePresenter<V> implements RegisterIPresenter<V> {

    @Override
    public void register(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> files) {
        Observable modelObservable = APIClient.getAPIClient().register(params, files);
        getMvpView().showLoading();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> {
                            getMvpView().hideLoading();
                            RegisterPresenter.this.getMvpView().onSuccess((User) trendsResponse);
                        },
                        (Consumer) throwable -> {
                            getMvpView().hideLoading();
                            RegisterPresenter.this.getMvpView().onError((Throwable) throwable);
                        });

    }

    @Override
    public void verifyMobileAlreadyExits(String params) {

        Observable modelObservable = APIClient.getAPIClient().verifyMobileAlreadyExits(params);
        getMvpView().showLoading();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> {
                            getMvpView().hideLoading();
                            Status status = (Status) trendsResponse;
                            if (status.getStatus()) {
                                Toast.makeText(getMvpView().activity(), "Mobile Already Registered", Toast.LENGTH_SHORT).show();
                            } else {
                                sendOTP(params);
                            }
                        },
                        (Consumer) throwable -> {
                            getMvpView().hideLoading();
                            RegisterPresenter.this.getMvpView().onError((Throwable) throwable);
                        });


    }

    @Override
    public void sendOTP(Object obj) {

        Observable modelObservable = APIClient.getAPIClient().sendOtp(obj);
        getMvpView().showLoading();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> {
                            getMvpView().hideLoading();
                            RegisterPresenter.this.getMvpView().onSuccess((MyOTP) trendsResponse);
                        },
                        (Consumer) throwable -> {
                            getMvpView().hideLoading();
                            RegisterPresenter.this.getMvpView().onError((Throwable) throwable);
                        });
    }



    @Override
    public void getSettings() {
        Observable modelObservable = APIClient.getAPIClient().getSettings();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->{
                    RegisterPresenter.this.getMvpView().onSuccess((SettingsResponse) response);
                }, (Consumer) throwable -> {
                    RegisterPresenter.this.getMvpView().onError((Throwable) throwable);
                });
    }
}
