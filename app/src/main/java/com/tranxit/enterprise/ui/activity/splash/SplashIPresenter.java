package com.tranxit.enterprise.ui.activity.splash;

import com.tranxit.enterprise.base.MvpPresenter;

public interface SplashIPresenter<V extends SplashIView> extends MvpPresenter<V> {
    void handlerCall();
}
