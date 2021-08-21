package com.tranxit.enterprise.ui.bottomsheetdialog.invoice_flow;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface InvoiceDialogIPresenter<V extends InvoiceDialogIView> extends MvpPresenter<V> {
    void statusUpdate(HashMap<String, Object> obj, Integer id);
}
