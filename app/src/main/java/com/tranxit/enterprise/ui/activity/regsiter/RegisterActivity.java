package com.tranxit.enterprise.ui.activity.regsiter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tranxit.enterprise.data.network.model.SettingsResponse;
import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.data.network.model.MyOTP;
import com.tranxit.enterprise.data.network.model.User;
import com.tranxit.enterprise.ui.activity.main.MainActivity;
import com.tranxit.enterprise.ui.activity.otp.OTPActivity;
import com.tranxit.enterprise.ui.activity.welcome.WelcomeActivity;
import com.tranxit.enterprise.ui.countrypicker.Country;
import com.tranxit.enterprise.ui.countrypicker.CountryPicker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

import static com.tranxit.enterprise.common.CommonValidation.Validation;
import static com.tranxit.enterprise.common.CommonValidation.isValidEmail;
import static com.tranxit.enterprise.common.CommonValidation.isValidPass;
import static com.tranxit.enterprise.common.CommonValidation.isValidPhone;
import static com.tranxit.enterprise.common.Constants.APP_REQUEST_CODE;

public class RegisterActivity extends BaseActivity implements RegisterIView {

    private static final String TAG = "RegisterActivity";
    private static final int PICK_OTP_VERIFY = 222;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtFirstName)
    EditText txtFirstName;
    @BindView(R.id.txtLastName)
    EditText txtLastName;
    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.lblTerms)
    TextView lblTerms;
    @BindView(R.id.mobile_layout)
    LinearLayout mobile_layout;
    @BindView(R.id.registration_layout)
    LinearLayout registration_layout;

    RegisterPresenter<RegisterActivity> presenter = new RegisterPresenter<>();

    private CountryPicker mCountryPicker;
    private String countryDialCode = "+60";
    private String countryCode = "MY";

    @BindView(R.id.ivCountry)
    ImageView ivCountry;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvEmergencyNoOne)
    EditText tvEmergencyNoOne;
    @BindView(R.id.tvEmergencyNoTwo)
    EditText tvEmergencyNoTwo;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.getSettings();
        setCountryList();

      /*  if (BuildConfig.DEBUG) {
            txtPhoneNumber.setText("9988776655");
            txtEmail.setText("bbb@bbb.bbb");
            txtFirstName.setText("Test");
            txtLastName.setText("bbb");
            tvEmergencyNoOne.setText("9988776655");
            txtPassword.setText("112233");
            txtConfirmPassword.setText("112233");
        }*/
    }

    private boolean validation() {


//        if (txtEmail.getText().toString().isEmpty()) {
//            Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
//            return false;
//        }
        if (Validation(txtPhoneNumber.getText().toString().trim())) {
            Toasty.error(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (isValidPhone(txtPhoneNumber.getText().toString().trim())) {
            Toasty.error(this, getString(R.string.validmobile), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (Validation(txtFirstName.getText().toString().trim())) {
            Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (Validation(txtLastName.getText().toString().trim())) {
            Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
            return false;
        }/* else if (txtPassword.getText().toString().length() < 6) {
            Toasty.error(this, getString(R.string.invalid_password_length), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (txtConfirmPassword.getText().toString().isEmpty()) {
            Toasty.error(this, getString(R.string.invalid_confirm_password), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
            Toasty.error(this, getString(R.string.password_should_be_same), Toast.LENGTH_SHORT, true).show();
            return false;
        } else if (!chkTerms.isChecked()) {
            Toasty.error(this, "Please Accept Terms and Conditions", Toast.LENGTH_SHORT, true).show();
            return false;

        }*/ else {
            return true;
        }


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

        Country country = getDeviceCountry(RegisterActivity.this);
        ivCountry.setImageResource(country.getFlag());
        tvCountry.setText(country.getDialCode());
        countryDialCode = country.getDialCode();
        countryCode = country.getCode();
    }

    void register() {

        //All the String parameters, you have to put like
        Map<String, RequestBody> map = new HashMap<>();

        map.put("first_name", toRequestBody(txtFirstName.getText().toString()));
        map.put("last_name", toRequestBody(txtLastName.getText().toString()));
        map.put("email", toRequestBody(txtEmail.getText().toString()));
        map.put("mobile", toRequestBody(txtPhoneNumber.getText().toString()));
        map.put("password", toRequestBody(txtPassword.getText().toString()));
        map.put("password_confirmation", toRequestBody(txtConfirmPassword.getText().toString()));
        map.put("emergency_contact1", toRequestBody(tvEmergencyNoOne.getText().toString()));
        map.put("emergency_contact2", toRequestBody(tvEmergencyNoTwo.getText().toString()));
        map.put("device_token", toRequestBody(SharedHelper.getKeyFCM(this, "device_token")));
        map.put("device_id", toRequestBody(SharedHelper.getKeyFCM(this, "device_id")));
        map.put("device_type", toRequestBody(BuildConfig.DEVICE_TYPE));
        map.put("country_code", toRequestBody(countryDialCode));

        List<MultipartBody.Part> parts = new ArrayList<>();
        presenter.register(map, parts);
    }

    @OnClick({R.id.next, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.next:
                /*if (mobile_layout.getVisibility() == View.VISIBLE) {
                    if (CommonValidation.Validation(txtPhoneNumber.getText().toString().trim())) {
                        Toasty.error(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT, true).show();
                        return;
                    } else if (CommonValidation.isValidPhone(txtPhoneNumber.getText().toString().trim())) {
                        Toasty.error(this, getString(R.string.validmobile), Toast.LENGTH_SHORT, true).show();
                        return;
                    }
                    presenter.verifyMobileAlreadyExits(txtPhoneNumber.getText().toString());
                } else */
            {
                if (Validation(txtPhoneNumber.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_mobile), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (isValidPhone(txtPhoneNumber.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.validmobile), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (isValidEmail(txtEmail.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (Validation(txtFirstName.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_first_name), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (Validation(txtLastName.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (Validation(tvEmergencyNoOne.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_emergency_contact), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (isValidPass(txtPassword.getText().toString().trim())) {
                    Toasty.error(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT, true).show();
                    return;
                } else if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                    Toasty.error(this, getString(R.string.invalid_last_name), Toast.LENGTH_SHORT, true).show();
                    return;
                }
                register();
            }

            break;
            case R.id.back:
//                onBackPressed();
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onSuccess(User user) {

        SharedHelper.putKey(this, "user_id", String.valueOf(user.getId()));
        SharedHelper.putKey(this, "access_token", user.getAccessToken());
        SharedHelper.putKey(this, "loggged_in", "true");


        Toasty.success(this, "Registered Successfully!", Toast.LENGTH_SHORT, true).show();
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void onSuccess(MyOTP otp) {

        Intent intent = new Intent(activity(), OTPActivity.class);
        intent.putExtra("mobile", txtPhoneNumber.getText().toString());
        intent.putExtra("otp", String.valueOf(otp.getOtp()));
        startActivityForResult(intent, PICK_OTP_VERIFY);

    }

    @Override
    public void onError(Throwable e) {

        hideLoading();

        HttpException error = (HttpException) e;


        try {

            String errorBody = error.response().errorBody().string();
            JSONObject jObjError = new JSONObject(errorBody);


            if (jObjError.has("email"))
                Toast.makeText(getApplicationContext(), jObjError.optString("email"), Toast.LENGTH_LONG).show();
            if (jObjError.has("error"))
                Toast.makeText(getApplicationContext(), jObjError.optString("error"), Toast.LENGTH_LONG).show();
            if (jObjError.has("message"))
                Toast.makeText(getApplicationContext(), jObjError.optString("error"), Toast.LENGTH_LONG).show();
            if (jObjError.has("mobile"))
                Toast.makeText(activity(), jObjError.optString("mobile"), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } catch (Exception e1) {
            Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onSuccess(SettingsResponse response) {
        SharedHelper.putKey(this, SharedHelper.GOOGLE_API_KEY, response.getApiKey());
    }

    public void fbPhoneLogin() {

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);

        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == APP_REQUEST_CODE && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {
                    Log.d("AccountKit", "onSuccess: Account Kit" + AccountKit.getCurrentAccessToken().getToken());
                    if (AccountKit.getCurrentAccessToken().getToken() != null) {
                        //SharedHelper.putKey(RegisterActivity.this, "account_kit_token", AccountKit.getCurrentAccessToken().getToken());
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        SharedHelper.putKey(RegisterActivity.this, "dial_code", phoneNumber.getCountryCode());
                        SharedHelper.putKey(RegisterActivity.this, "mobile", phoneNumber.getPhoneNumber());

                        txtPhoneNumber.setText(SharedHelper.getKey(RegisterActivity.this, "mobile"));

                        register();
                    }
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.e("AccountKit", "onError: Account Kit" + accountKitError);
                }
            });

        } else if (requestCode == PICK_OTP_VERIFY && resultCode == Activity.RESULT_OK) {
            registration_layout.setVisibility(View.VISIBLE);
            mobile_layout.setVisibility(View.GONE);

            Toast.makeText(this, "Thanks your Mobile is successfully verified, Please enter your First Name and Last Name to create your account", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
