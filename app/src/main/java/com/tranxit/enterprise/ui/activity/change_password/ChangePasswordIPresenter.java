package com.tranxit.enterprise.ui.activity.change_password;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface ChangePasswordIPresenter<V extends ChangePasswordIView> extends MvpPresenter<V> {
    void changePassword(HashMap<String, Object> obj);
}
