package com.tranxit.enterprise.ui.activity.reset_password;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.ui.activity.welcome.WelcomeActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ResetActivity extends BaseActivity implements ResetIView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtOTP)
    EditText txtOTP;
    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.btnDone)
    FloatingActionButton btnDone;

    ResetPresenter<ResetActivity> presenter = new ResetPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        txtEmail.setText(SharedHelper.getKey(this, "txtEmail"));
    }

    @OnClick({R.id.back, R.id.btnDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btnDone:
                /*if (txtEmail.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                } else*/ if (!SharedHelper.getKey(this, "otp").equals(txtOTP.getText().toString())) {
                    Toasty.error(this, "Please enter Correct OTP", Toast.LENGTH_SHORT, true).show();
                } else if (txtPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_old_password), Toast.LENGTH_SHORT, true).show();
                } else if (txtNewPassword.getText().toString().isEmpty()) {
                    Toasty.error(this, getString(R.string.invalid_new_password), Toast.LENGTH_SHORT, true).show();
                } else if (!txtPassword.getText().toString().equals(txtNewPassword.getText().toString())) {
                    Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
                } else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", SharedHelper.getKey(this, "id_"));
                    map.put("password", txtNewPassword.getText().toString());
                    map.put("password_confirmation", txtNewPassword.getText().toString());
                    presenter.reset(map);
                }
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        hideLoading();
        Toasty.success(this, "Password Updated!", Toast.LENGTH_SHORT, true).show();
        Intent goToLogin = new Intent(activity(), WelcomeActivity.class);
        goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity().startActivity(goToLogin);
        activity().finish();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }
}
