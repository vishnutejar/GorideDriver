package com.tranxit.enterprise.ui.activity.sociallogin;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.Token;

public interface SocialLoginIView extends MvpView {
    void onSuccess(Token token);

    void onError(Throwable e);
}
