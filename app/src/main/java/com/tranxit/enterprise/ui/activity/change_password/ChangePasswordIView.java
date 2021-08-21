package com.tranxit.enterprise.ui.activity.change_password;

import com.tranxit.enterprise.base.MvpView;

public interface ChangePasswordIView extends MvpView {
    void onSuccess(Object object);
    void onError(Throwable e);
}
