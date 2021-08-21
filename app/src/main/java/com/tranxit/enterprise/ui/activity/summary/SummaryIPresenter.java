package com.tranxit.enterprise.ui.activity.summary;


import com.tranxit.enterprise.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {
    void getSummary();
}
