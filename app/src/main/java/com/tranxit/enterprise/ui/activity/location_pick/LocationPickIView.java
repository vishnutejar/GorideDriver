package com.tranxit.enterprise.ui.activity.location_pick;


import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.AddressResponse;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface LocationPickIView extends MvpView {

    void onSuccess(AddressResponse address);
    void onError(Throwable e);
}
