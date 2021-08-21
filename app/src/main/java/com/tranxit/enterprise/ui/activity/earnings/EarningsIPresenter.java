package com.tranxit.enterprise.ui.activity.earnings;


import com.tranxit.enterprise.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {
    void getEarnings();
}
