package com.tranxit.enterprise.ui.fragment.offline;

import com.tranxit.enterprise.base.MvpView;

public interface OfflineIView extends MvpView {
    void onSuccess(Object object);
    void onError(Throwable e);
}
