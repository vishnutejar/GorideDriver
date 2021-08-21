package com.tranxit.enterprise.ui.fragment.offline;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OfflinePresenter<V extends OfflineIView> extends BasePresenter<V> implements OfflineIPresenter<V> {

    @Override
    public void providerAvailable(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().providerAvailable(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }
}
