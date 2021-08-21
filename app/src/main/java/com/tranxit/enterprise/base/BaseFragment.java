package com.tranxit.enterprise.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Lenovo on 02-04-2018.
 */

public abstract class BaseFragment extends Fragment implements MvpView {

    View view;
    ProgressDialog progressDialog;
    public abstract int getLayoutId();
    public  abstract View initView(View view);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }

        progressDialog = new ProgressDialog(activity());
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        return view;
    }



    @Override
    public FragmentActivity activity() {
        return getActivity();
    }



    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }



}
