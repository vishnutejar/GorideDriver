package com.tranxit.enterprise.ui.activity.regsiter;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.MyOTP;
import com.tranxit.enterprise.data.network.model.SettingsResponse;
import com.tranxit.enterprise.data.network.model.User;

public interface RegisterIView extends MvpView {
    void onSuccess(User user);

    void onSuccess(MyOTP otp);

    void onError(Throwable e);

    void onSuccess(SettingsResponse response);
}
