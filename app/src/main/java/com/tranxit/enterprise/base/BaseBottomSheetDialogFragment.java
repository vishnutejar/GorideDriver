package com.tranxit.enterprise.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tranxit.enterprise.MvpApplication;
import com.tranxit.enterprise.common.SharedHelper;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements MvpView  {


    View view;


    public abstract int getLayoutId();


    ProgressDialog progressDialog;

    public abstract void initView(View view);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
    public Activity activity() {
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

    public NumberFormat getNumberFormat() {
        String currencyCode = SharedHelper.getKey(MvpApplication.getInstance(), "currency_code", null);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        if (currencyCode != null)
            numberFormat.setCurrency(Currency.getInstance(currencyCode));
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat;
    }
}
