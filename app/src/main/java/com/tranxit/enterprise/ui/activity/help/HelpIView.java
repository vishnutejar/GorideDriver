package com.tranxit.enterprise.ui.activity.help;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.Help;

public interface HelpIView extends MvpView {
    void onSuccess(Help object);

    void onError(Throwable e);
}
