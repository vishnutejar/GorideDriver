package com.tranxit.enterprise.ui.activity.summary;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.Summary;

public interface SummaryIView extends MvpView {
    void onSuccess(Summary object);

    void onError(Throwable e);
}
