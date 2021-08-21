package com.tranxit.enterprise.ui.activity.forgot_password;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotIPresenter<V extends ForgotIView> extends MvpPresenter<V> {
    void forgot(HashMap<String, Object> obj);
}
