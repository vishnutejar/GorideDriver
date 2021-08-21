package com.tranxit.enterprise.ui.activity.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.data.network.model.User;
import com.tranxit.enterprise.ui.activity.forgot_password.ForgotActivity;
import com.tranxit.enterprise.ui.activity.main.MainActivity;
import com.tranxit.enterprise.ui.activity.regsiter.RegisterActivity;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.HttpException;

public class PasswordActivity extends BaseActivity implements PasswordIView {

    PasswordPresenter<PasswordActivity> presenter = new PasswordPresenter<>();
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.next)
    FloatingActionButton next;

    String email, countryDialCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            countryDialCode = extras.getString("country_code");
        }

        if (BuildConfig.DEBUG) password.setText("123456");
    }

    @OnClick({R.id.back, R.id.sign_up, R.id.forgot_password, R.id.next, R.id.show_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                activity().onBackPressed();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forgot_password:
                startActivity(new Intent(this, ForgotActivity.class).putExtra("email", email));
                break;
            case R.id.next:
                login();
                break;
            case R.id.show_password:
                if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
        }
    }

    private void login() {
        if (password.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (email.isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT, true).show();
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", email);
        map.put("country_code", countryDialCode);
        map.put("password", password.getText().toString());
        map.put("device_id", SharedHelper.getKeyFCM(this, "device_id"));
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("device_token", SharedHelper.getKeyFCM(this, "device_token"));
        showLoading();
        presenter.login(map);
    }

    @Override
    public void onSuccess(User user) {
        hideLoading();
        SharedHelper.putKey(this, "access_token", user.getAccessToken());
        SharedHelper.putKey(this, "user_id", String.valueOf(user.getId()));
        SharedHelper.putKey(this, "loggged_in", "true");
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
        HttpException error = (HttpException) e;
        try {
            String errorBody = error.response().errorBody().string();
            JSONObject jObjError = new JSONObject(errorBody);
            if (jObjError.has("device_type"))
                Toast.makeText(getApplicationContext(), jObjError.optString("device_type"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("device_token"))
                Toast.makeText(getApplicationContext(), jObjError.optString("device_token"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("device_id"))
                Toast.makeText(getApplicationContext(), jObjError.optString("device_id"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("login_by"))
                Toast.makeText(getApplicationContext(), jObjError.optString("login_by"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("first_name"))
                Toast.makeText(getApplicationContext(), jObjError.optString("first_name"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("last_name"))
                Toast.makeText(getApplicationContext(), jObjError.optString("last_name"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("email"))
                Toast.makeText(getApplicationContext(), jObjError.optString("email"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("mobile"))
                Toast.makeText(getApplicationContext(), jObjError.optString("mobile"), Toast.LENGTH_LONG).show();
            else if (jObjError.has("password"))
                Toast.makeText(getApplicationContext(), jObjError.optString("password"), Toast.LENGTH_LONG).show();
            if (jObjError.has("error"))
                Toast.makeText(getApplicationContext(), jObjError.optString("error"), Toast.LENGTH_LONG).show();
        } catch (Exception e1) {
            Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
