package com.tranxit.enterprise.ui.activity.past_detail;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.HistoryDetail;

public interface PastTripDetailIView extends MvpView {
    void onSuccess(HistoryDetail historyDetail);

    void onError(Throwable e);
}
