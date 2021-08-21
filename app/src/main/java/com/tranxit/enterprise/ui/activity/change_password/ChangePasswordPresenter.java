package com.tranxit.enterprise.ui.activity.change_password;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordPresenter<V extends ChangePasswordIView> extends BasePresenter<V> implements ChangePasswordIPresenter<V> {

    @Override
    public void changePassword(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().changePassword(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }
}
