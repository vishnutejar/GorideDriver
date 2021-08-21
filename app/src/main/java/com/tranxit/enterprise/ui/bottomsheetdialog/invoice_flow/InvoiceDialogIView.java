package com.tranxit.enterprise.ui.bottomsheetdialog.invoice_flow;

import com.tranxit.enterprise.base.MvpView;

public interface InvoiceDialogIView extends MvpView {
    void onSuccess(Object object);
    void onError(Throwable e);
}
