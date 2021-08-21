package com.tranxit.enterprise.ui.activity.email;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface EmailIPresenter<V extends EmailIView> extends MvpPresenter<V> {
    void login(HashMap<String, Object> obj);
    void verifyOTP(HashMap<String, Object> obj);
}
