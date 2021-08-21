package com.tranxit.enterprise.ui.activity.upcoming_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.Constants;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.data.network.model.HistoryDetail;
import com.tranxit.enterprise.data.network.model.User_Past;
import com.tranxit.enterprise.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UpcomingTripDetailActivity extends BaseActivity implements UpcomingTripDetailIView, EasyPermissions.PermissionCallbacks {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.call)
    Button call;
    @BindView(R.id.static_map)
    ImageView staticMap;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.first_name)
    TextView firstName;
    @BindView(R.id.rating)
    AppCompatRatingBar rating;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.schedule_at)
    TextView scheduleAt;
    @BindView(R.id.lblSource)
    TextView lblSource;
    @BindView(R.id.lblDestination)
    TextView lblDestination;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.payable)
    TextView payable;
    Integer requestId = null;
    UpcomingTripDetailPresenter<UpcomingTripDetailActivity> presenter = new UpcomingTripDetailPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_upcoming_trip_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            requestId = bundle.getInt("request_id");
            presenter.getUpcomingDetail(requestId);
        }

    }

    void initPayment(String mode) {
        switch (mode) {
            case "CASH":
                paymentMode.setText(getString(R.string.cash));
                paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                break;
            case "CARD":
                paymentMode.setText(getString(R.string.card));
                paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visa, 0, 0, 0);
                break;
            case "PAYPAL":
                paymentMode.setText(getString(R.string.paypal));
                paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_paypal, 0, 0, 0);
                break;
            case "WALLET":
                paymentMode.setText(getString(R.string.wallet));
                paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wallet, 0, 0, 0);
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.cancel, R.id.call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (requestId != null) {
                    SharedHelper.putKey(getBaseContext(), "cancel_id", String.valueOf(requestId));
                    cancelRequestPopup();
                }
                break;
            case R.id.call:
                callTask();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(HistoryDetail historyDetail) {

        BaseActivity.DATUM_history_detail = historyDetail;

        bookingId.setText(historyDetail.getBookingId());
        scheduleAt.setText(historyDetail.getScheduleAt());
        lblSource.setText(historyDetail.getSAddress());
        lblDestination.setText(historyDetail.getDAddress());
        Glide.with(activity()).load(historyDetail.getStaticMap()).apply(RequestOptions.placeholderOf(R.drawable.ic_launcher_background).dontAnimate().error(R.drawable.ic_launcher_background)).into(staticMap);

        initPayment(historyDetail.getPaymentMode());

        User_Past user = historyDetail.getUser();
        if (user != null) {
            firstName.setText(user.getFirstName());
            Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL + user.getPicture()).apply(RequestOptions.placeholderOf(R.drawable.user).dontAnimate().error(R.drawable.user)).into(avatar);

        }


    }

    @Override
    public void onError(Throwable e) {

    }


    @SuppressLint("MissingPermission")
    void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }


    void cancelRequestPopup() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity());
        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure want to Cancel this request?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                        cancelDialogFragment.show(getSupportFragmentManager(), cancelDialogFragment.getTag());


                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
