package com.tranxit.enterprise.ui.bottomsheetdialog.cancel;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CancelDialogPresenter<V extends CancelDialogIView> extends BasePresenter<V> implements CancelDialogIPresenter<V> {

    @Override
    public void cancelRequest(HashMap<String, Object> obj) {
        Observable modelObservable = APIClient.getAPIClient().cancelRequest(obj);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccessCancel(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));
    }
}
