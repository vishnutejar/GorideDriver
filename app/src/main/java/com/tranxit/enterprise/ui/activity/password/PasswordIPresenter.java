package com.tranxit.enterprise.ui.activity.password;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface PasswordIPresenter<V extends PasswordIView> extends MvpPresenter<V> {
    void login(HashMap<String, Object> obj);
}
