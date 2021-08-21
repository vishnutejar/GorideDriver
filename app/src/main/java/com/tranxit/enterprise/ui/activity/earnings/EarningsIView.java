package com.tranxit.enterprise.ui.activity.earnings;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {
    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
