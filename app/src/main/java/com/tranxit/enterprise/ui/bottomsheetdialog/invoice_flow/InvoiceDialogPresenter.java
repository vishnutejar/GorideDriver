package com.tranxit.enterprise.ui.bottomsheetdialog.invoice_flow;

import com.tranxit.enterprise.base.BasePresenter;
import com.tranxit.enterprise.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InvoiceDialogPresenter<V extends InvoiceDialogIView> extends BasePresenter<V> implements InvoiceDialogIPresenter<V> {

    @Override
    public void statusUpdate(HashMap<String, Object> obj, Integer id) {
        Observable modelObservable = APIClient.getAPIClient().updateRequest(obj, id);
        modelObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trendsResponse -> getMvpView().onSuccess(trendsResponse),
                        throwable -> getMvpView().onError((Throwable) throwable));

    }
}
