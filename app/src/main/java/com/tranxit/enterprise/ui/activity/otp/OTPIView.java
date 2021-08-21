package com.tranxit.enterprise.ui.activity.otp;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.MyOTP;

public interface OTPIView extends MvpView {
    void onSuccess(MyOTP otp);

    void onError(Throwable e);
}
