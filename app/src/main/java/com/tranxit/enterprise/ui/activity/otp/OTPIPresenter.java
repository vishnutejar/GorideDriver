package com.tranxit.enterprise.ui.activity.otp;

import com.tranxit.enterprise.base.MvpPresenter;


public interface OTPIPresenter<V extends OTPIView> extends MvpPresenter<V> {
    void sendOTP(Object obj);
    void sendVoiceOTP(Object obj);
}
