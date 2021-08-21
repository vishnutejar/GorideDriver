package com.tranxit.enterprise.ui.fragment.past;


import com.tranxit.enterprise.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {
    void getHistory();
}
