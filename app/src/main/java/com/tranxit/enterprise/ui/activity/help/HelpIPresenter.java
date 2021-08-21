package com.tranxit.enterprise.ui.activity.help;


import com.tranxit.enterprise.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {
    void getHelp();
}
