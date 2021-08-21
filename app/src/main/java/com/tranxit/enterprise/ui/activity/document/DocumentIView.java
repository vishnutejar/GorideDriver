package com.tranxit.enterprise.ui.activity.document;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.ui.adapter.DocumentAdapter;

public interface DocumentIView extends MvpView {
    void onSuccess(DocumentAdapter adapter);
}
