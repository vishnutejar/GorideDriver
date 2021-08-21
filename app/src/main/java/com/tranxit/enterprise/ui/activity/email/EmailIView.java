package com.tranxit.enterprise.ui.activity.email;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.User;

public interface EmailIView extends MvpView {
    void onSuccess(User token);

    void onError(Throwable e);
}
