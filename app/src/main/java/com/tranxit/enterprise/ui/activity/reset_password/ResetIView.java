package com.tranxit.enterprise.ui.activity.reset_password;

import com.tranxit.enterprise.base.MvpView;

public interface ResetIView extends MvpView{
    void onSuccess(Object object);
    void onError(Throwable e);
}
