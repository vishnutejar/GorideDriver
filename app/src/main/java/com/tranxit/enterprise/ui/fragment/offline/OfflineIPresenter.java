package com.tranxit.enterprise.ui.fragment.offline;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface OfflineIPresenter<V extends OfflineIView> extends MvpPresenter<V> {
    void providerAvailable(HashMap<String, Object> obj);
}
