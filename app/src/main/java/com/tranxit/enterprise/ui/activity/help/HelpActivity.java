package com.tranxit.enterprise.ui.activity.help;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.Constants;
import com.tranxit.enterprise.data.network.model.Help;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HelpActivity extends BaseActivity implements HelpIView, EasyPermissions.PermissionCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgCall)
    ImageView imgCall;
    @BindView(R.id.imgMail)
    ImageView imgMail;
    @BindView(R.id.imgWeb)
    ImageView imgWeb;

    HelpPresenter<HelpActivity> presenter = new HelpPresenter<>();
    Help help;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.getHelp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(Help helpDetails) {
        help = helpDetails;
    }

    @Override
    public void onError(Throwable e) {

    }

    @OnClick({R.id.imgCall, R.id.imgMail, R.id.imgWeb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCall:
                if (help != null) {
                    makeCall(help.getContactNumber());
                }
                break;
            case R.id.imgMail:
                if (help == null) {
                    return;
                }
                String to = help.getContactEmail();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "-" + getString(R.string.help));
                startActivity(Intent.createChooser(intent, "Send feedback"));
                break;
            case R.id.imgWeb:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.BASE_URL+"help"));
                startActivity(browserIntent);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    void makeCall(String number) {
        if (number == null) {
            return;
        }

//        if (hasCallPermission()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
//        } else {
//            EasyPermissions.requestPermissions(
//                    this, "Please give the CALL PERMISSION!",
//                    Constants.RC_CALL_PHONE,
//                    Manifest.permission.CALL_PHONE);
//        }
    }


    private boolean hasCallPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE);
    }

    @AfterPermissionGranted(Constants.RC_CALL_PHONE)
    public void callTask() {
        if (hasCallPermission()) {
            makeCall(DATUM_history_detail.getUser().getMobile());
        } else {
            EasyPermissions.requestPermissions(
                    this, "Please give the CALL PERMISSION!",
                    Constants.RC_CALL_PHONE,
                    Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
