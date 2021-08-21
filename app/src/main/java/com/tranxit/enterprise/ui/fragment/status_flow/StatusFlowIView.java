package com.tranxit.enterprise.ui.fragment.status_flow;

import com.tranxit.enterprise.base.MvpView;

public interface StatusFlowIView extends MvpView {
    void onSuccess(Object object);
    void onError(Throwable e);
}
