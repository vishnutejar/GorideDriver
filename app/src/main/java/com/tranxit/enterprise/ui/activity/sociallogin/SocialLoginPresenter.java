package com.tranxit.enterprise.ui.activity.sociallogin;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.model.Token;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SocialLoginPresenter<V extends SocialLoginIView> extends BasePresenter<V> implements SocialLoginIPresenter<V> {

    @Override
    public void loginGoogle(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().loginGoogle(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((Token) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));
    }
    @Override
    public void loginFacebook(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().loginFacebook(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((Token) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));
    }
}
