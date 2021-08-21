package com.tranxit.enterprise.ui.activity.past_detail;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;
import com.tranxit.enterprise.data.network.model.HistoryDetail;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PastTripDetailPresenter<V extends PastTripDetailIView> extends BasePresenter<V> implements PastTripDetailIPresenter<V> {

    @Override
    public void getPastTripDetail(Object request_id) {
        Observable modelObservable = APIClient.getAPIClient().getHistoryDetail(request_id);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess((HistoryDetail) trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }
}
