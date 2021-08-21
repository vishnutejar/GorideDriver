package com.tranxit.enterprise.ui.bottomsheetdialog.rating;

import com.tranxit.enterprise.base.MvpView;
import com.tranxit.enterprise.data.network.model.Rating;

public interface RatingDialogIView extends MvpView {
    void onSuccess(Rating rating);
    void onError(Throwable e);
}
