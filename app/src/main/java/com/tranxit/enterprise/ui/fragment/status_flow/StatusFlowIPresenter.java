package com.tranxit.enterprise.ui.fragment.status_flow;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface StatusFlowIPresenter<V extends StatusFlowIView> extends MvpPresenter<V> {
    void statusUpdate(HashMap<String, Object> obj, Integer id);
}
