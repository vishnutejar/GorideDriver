package com.tranxit.enterprise.ui.fragment.past;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {
    void onSuccess(List<HistoryList> historyList);
    void onError(Throwable e);
}
