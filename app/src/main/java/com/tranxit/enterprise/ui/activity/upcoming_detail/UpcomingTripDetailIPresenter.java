package com.tranxit.enterprise.ui.activity.upcoming_detail;


import com.tranxit.enterprise.base.MvpPresenter;

public interface UpcomingTripDetailIPresenter<V extends UpcomingTripDetailIView> extends MvpPresenter<V> {
    void getUpcomingDetail(Object request_id);
}
