package com.tranxit.enterprise.base;

import android.app.Activity;

import com.tranxit.enterprise.MvpApplication;

public interface MvpPresenter<V extends MvpView> {

    Activity activity();

    MvpApplication appContext();

    void attachView(V mvpView);

    void detachView();

}
