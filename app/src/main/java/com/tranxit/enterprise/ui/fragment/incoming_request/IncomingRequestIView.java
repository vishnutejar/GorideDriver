package com.tranxit.enterprise.ui.fragment.incoming_request;

import com.tranxit.enterprise.base.MvpView;

public interface IncomingRequestIView extends MvpView {
    void onSuccessAccept(Object responseBody);
    void onSuccessCancel(Object object);
    void onError(Throwable e);
}
