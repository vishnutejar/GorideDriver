package com.tranxit.enterprise.ui.fragment.upcoming;


import com.tranxit.enterprise.base.MvpPresenter;

public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {
    void getUpcoming();
}
