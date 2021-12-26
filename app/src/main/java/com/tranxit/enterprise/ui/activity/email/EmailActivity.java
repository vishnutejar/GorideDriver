package com.tranxit.enterprise.ui.activity.email;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goride.provider.BuildConfig;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.data.network.model.User;
import com.tranxit.enterprise.ui.activity.forgot_password.ForgotActivity;
import com.tranxit.enterprise.ui.activity.main.MainActivity;
import com.tranxit.enterprise.ui.activity.otp.OTPActivity;
import com.tranxit.enterprise.ui.activity.regsiter.RegisterActivity;
import com.tranxit.enterprise.ui.activity.welcome.WelcomeActivity;
import com.tranxit.enterprise.ui.countrypicker.Country;
import com.tranxit.enterprise.ui.countrypicker.CountryPicker;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.HttpException;
import retrofit2.Response;

public class EmailActivity extends BaseActivity implements EmailIView {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.tv_create_account)
    TextView tv_create_account;

    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;

    @BindView(R.id.txt_ride)
    TextView txt_ride;

    @BindView(R.id.next)
    Button next;

    @BindView(R.id.password)
    EditText password;
    EmailIPresenter<EmailActivity> presenter = new EmailPresenter<>();

    private CountryPicker mCountryPicker;
    private String countryDialCode = "+60";
    private String countryCode = "MY";

    @BindView(R.id.ivCountry)
    ImageView ivCountry;
    @BindView(R.id.tvCountry)
    TextView tvCountry;

    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        if (BuildConfig.DEBUG){
            email.setText("2025550096");
            password.setText("123456");
        }
        setCountryList();
        tv_forgot_password.setText(Html.fromHtml("<p><u>Forget Password?</u></p>"));
        txt_ride.setText(Html.fromHtml("<font color=#D7251A>D</font><font color=#414141>river</font>"));

    }

    private void setCountryList() {
        mCountryPicker = CountryPicker.newInstance("Select Country");
        List<Country> countryList = Country.getAllCountries();
        Collections.sort(countryList, (s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
        mCountryPicker.setCountriesList(countryList);

        mCountryPicker.setListener((name, code, dialCode, flagDrawableResID) -> {
            tvCountry.setText(dialCode);
            countryDialCode = dialCode;
            ivCountry.setImageResource(flagDrawableResID);
            mCountryPicker.dismiss();
        });

        ivCountry.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        tvCountry.setOnClickListener(v -> mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER"));

        Country country = getDeviceCountry(EmailActivity.this);
        ivCountry.setImageResource(country.getFlag());
        tvCountry.setText(country.getDialCode());
        countryDialCode = country.getDialCode();
        countryCode = country.getCode();
    }

    @OnClick({R.id.tv_create_account, R.id.tv_forgot_password,R.id.next, R.id.show_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_password:
                if (!email.getText().toString().isEmpty()) {
                    startActivity(new Intent(this, ForgotActivity.class).putExtra("email", email.getText().toString()));
                }else {
                    Toasty.error(this, getString(R.string.phonenumberValidation), Toast.LENGTH_SHORT, true).show();

                }
                break;
            case R.id.tv_create_account:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.show_password:
                if (password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.next:
                login();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(EmailActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private void login() {
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.phonenumberValidation), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
            return;
        }

      /*  HashMap<String, Object> map = new HashMap<>();
        map.put("grant_type", "password");
        map.put("mobile", email.getText().toString());
        map.put("password", "123456");
        map.put("client_secret", BuildConfig.CLIENT_SECRET);
        map.put("client_id", BuildConfig.CLIENT_ID);
        map.put("device_token", SharedHelper.getKeyFCM(this, "device_token"));
        map.put("device_id", SharedHelper.getKeyFCM(this, "device_id"));
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("scope", "");
        map.put("country_code", countryDialCode);
        showLoading();
        presenter.login(map);*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("mobile", email.getText().toString());
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
        Toasty.success(activity(), "Loggedin Successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
        if (e instanceof HttpException) {
            Response response = ((HttpException) e).response();
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                if (jObjError.has("message"))
                    Toast.makeText(activity(), jObjError.optString("message"), Toast.LENGTH_SHORT).show();
                if (jObjError.has("otp")) {
                    Intent intent = new Intent(activity(), OTPActivity.class);
                    intent.putExtra("mobile", email.getText().toString());
                    intent.putExtra("otp", String.valueOf(jObjError.optString("otp")));
                    startActivityForResult(intent, PICK_OTP_VERIFY);
                }
            } catch (Exception exp) {
                Log.e("Error", exp.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_OTP_VERIFY && resultCode == Activity.RESULT_OK) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("otp", data.getStringExtra("otp"));
            map.put("mobile", email.getText().toString());
            map.put("device_token", SharedHelper.getKeyFCM(this, "device_token"));
            map.put("device_id", SharedHelper.getKeyFCM(this, "device_id"));
            map.put("grant_type", "password");
            map.put("password", "123456");
            map.put("client_secret", BuildConfig.CLIENT_SECRET);
            map.put("client_id", BuildConfig.CLIENT_ID);
            map.put("device_type", BuildConfig.DEVICE_TYPE);
            showLoading();
            presenter.verifyOTP(map);
            Toast.makeText(this, "Thanks your Mobile is successfully verified", Toast.LENGTH_SHORT).show();
        }
    }

}
