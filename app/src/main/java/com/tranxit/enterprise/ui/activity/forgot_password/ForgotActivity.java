package com.tranxit.enterprise.ui.activity.forgot_password;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.data.network.model.ForgotResponse;
import com.tranxit.enterprise.ui.activity.reset_password.ResetActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ForgotActivity extends BaseActivity implements ForgotIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.title)
    TextView title;

    String email = "";

    ForgotPresenter<ForgotActivity> presenter = new ForgotPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            txtEmail.setText(email);
        }
        title.setText(getText(R.string.forgot_password));
    }

    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.next:
                if (txtEmail.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                } else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("mobile", txtEmail.getText().toString());
                    presenter.forgot(map);
                }
                break;
        }
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        hideLoading();
        SharedHelper.putKey(this, "txtEmail", txtEmail.getText().toString());
        SharedHelper.putKey(this, "otp", String.valueOf(forgotResponse.getProvider().getOtp()));
        SharedHelper.putKey(this, "id_", String.valueOf(forgotResponse.getProvider().getId()));

        Toasty.success(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(this, ResetActivity.class));
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }
}
