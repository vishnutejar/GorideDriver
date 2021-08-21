package com.tranxit.enterprise.ui.bottomsheetdialog.cancel;

import com.tranxit.enterprise.base.MvpView;

public interface CancelDialogIView extends MvpView {

    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
