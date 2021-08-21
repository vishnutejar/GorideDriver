package com.tranxit.enterprise.ui.activity.earnings;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.model.EarningsList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EarningsPresenter<V extends EarningsIView> extends BasePresenter<V> implements EarningsIPresenter<V> {

    @Override
    public void getEarnings() {
        Observable modelObservable = APIClient.getAPIClient().getEarnings();
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((EarningsList) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));
    }
}
