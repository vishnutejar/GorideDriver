package com.tranxit.enterprise.ui.activity.password;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.User;

public interface PasswordIView extends MvpView {
    void onSuccess(User object);

    void onError(Throwable e);
}
