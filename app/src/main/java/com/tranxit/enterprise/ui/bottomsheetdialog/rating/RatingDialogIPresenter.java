package com.tranxit.enterprise.ui.bottomsheetdialog.rating;

import com.tranxit.enterprise.base.MvpPresenter;

import java.util.HashMap;

public interface RatingDialogIPresenter<V extends RatingDialogIView> extends MvpPresenter<V> {
    void rate(HashMap<String, Object> obj, Integer id);
}
