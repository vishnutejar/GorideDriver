package com.tranxit.enterprise.ui.activity.past_detail;


import com.tranxit.enterprise.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {
    void getPastTripDetail(Object request_id);
}
