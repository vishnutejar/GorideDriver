package com.tranxit.enterprise.ui.activity.upcoming_detail;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.HistoryDetail;

public interface UpcomingTripDetailIView extends MvpView {
    void onSuccess(HistoryDetail historyDetail);

    void onError(Throwable e);
}
