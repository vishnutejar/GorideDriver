package com.tranxit.enterprise.ui.activity.summary;


import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.model.Summary;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SummaryPresenter<V extends SummaryIView> extends BasePresenter<V> implements SummaryIPresenter<V> {

    @Override
    public void getSummary() {
        Observable modelObservable = APIClient.getAPIClient().getSummary("");
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((Summary) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }
}
