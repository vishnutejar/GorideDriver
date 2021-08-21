package com.tranxit.enterprise.ui.activity.profile;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.User;

public interface ProfileIView extends MvpView {
    void onSuccess(User user);
    void onSuccessUser(User user);
    void onError(Throwable e);
}
