package com.tranxit.enterprise.ui.fragment.upcoming;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.HistoryList;

import java.util.List;

public interface UpcomingTripIView extends MvpView {
    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
