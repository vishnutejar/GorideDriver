package com.tranxit.enterprise.ui.activity.forgot_password;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.ForgotResponse;

public interface ForgotIView extends MvpView {
    void onSuccess(ForgotResponse forgotResponse);
    void onError(Throwable e);
}
