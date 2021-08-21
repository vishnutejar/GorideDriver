package com.tranxit.enterprise.ui.bottomsheetdialog.cancel;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);
}
