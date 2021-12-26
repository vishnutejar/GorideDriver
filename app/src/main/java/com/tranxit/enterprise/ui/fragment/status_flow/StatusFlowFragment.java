package com.tranxit.enterprise.ui.fragment.status_flow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chaos.view.PinView;
import com.google.android.gms.maps.model.LatLng;
import com.goride.provider.BuildConfig;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.base.BaseFragment;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.common.Utilities;
import com.tranxit.enterprise.common.chat.ChatActivity;
import com.tranxit.enterprise.data.network.model.Request_;
import com.tranxit.enterprise.data.network.model.UserGetResponse;
import com.tranxit.enterprise.ui.activity.main.MainActivity;
import com.tranxit.enterprise.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tranxit.enterprise.base.BaseActivity.DATUM;

public class StatusFlowFragment extends BaseFragment implements StatusFlowIView {


    StatusFlowPresenter<StatusFlowFragment> presenter = new StatusFlowPresenter<>();
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_rating)
    RatingBar userRating;
    @BindView(R.id.imgCall)
    ImageView imgCall;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnStatus)
    Button btnStatus;
    @BindView(R.id.status_arrived_img)
    CircleImageView statusArrivedImg;
    @BindView(R.id.status_picked_up_img)
    CircleImageView statusPickedUpImg;
    @BindView(R.id.status_finished_img)
    CircleImageView statusFinishedImg;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.imgMsg)
    ImageView imgMsg;
    Unbinder unbinder;
    Request_ data = null;
    AlertDialog otpDialog;
    String STATUS = "";
    Context thisContext;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_status_flow;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        this.thisContext = getContext();
        init();
        return view;
    }

    private void init() {
        data = BaseActivity.DATUM;
        if (data != null) {
            Utilities.printV("data===>", data.getStatus());
            changeFlow(data.getStatus());


            /*if (data.getSLatitude() != null && data.getSLongitude() != null && data.getDLatitude() != null && data.getDLongitude() != null) {
                LatLng origin = new LatLng(data.getSLatitude(), data.getSLongitude());
                LatLng destination = new LatLng(data.getDLatitude(), data.getDLongitude());
                ((MainActivity) thisContext).drawRoute(origin, destination, false);
            }*/
        }
    }

    @OnClick({R.id.imgCall, R.id.btnCancel, R.id.btnStatus, R.id.imgMsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgCall:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getUser().getMobile()));
                startActivity(intent);
                break;
            case R.id.btnCancel:
                SharedHelper.putKey(thisContext, "cancel_id", String.valueOf(data.getId()));
                cancelRequestPopup();
                break;
            case R.id.btnStatus:
                if (STATUS.equalsIgnoreCase("PICKEDUP")) {
                    if (data.getOtp() != null) {
                        showOTP();
                    } else {
                        statusUpdateCall(STATUS);
                    }
                } else if (STATUS.equalsIgnoreCase("DROPPED")) {
                    confirmPopup();
                } else {
                    statusUpdateCall(STATUS);
                }
                break;
            case R.id.imgMsg:
                if (DATUM != null) {
                    Intent i = new Intent(thisContext, ChatActivity.class);
                    i.putExtra("request_id", String.valueOf(DATUM.getId()));
                    i.putExtra("user_id", String.valueOf(DATUM.getUserId()));
                    startActivity(i);
                }
                break;
        }
    }


    public void changeFlow(String status) {

        btnCancel.setVisibility(View.GONE);
        if (data.getUser() != null) {
            UserGetResponse user = data.getUser();
            userName.setText(user.getFirstName() + " " + user.getLastName());
            userRating.setRating(Float.parseFloat(user.getRating()));
            Glide.with(thisContext).load(BuildConfig.BASE_IMAGE_URL + data.getUser().getPicture()).apply(RequestOptions.placeholderOf(R.drawable.user).dontAnimate().error(R.drawable.user)).into(userImg);
        }


        switch (status) {
            case "ACCEPTED":
                btnStatus.setText("ARRIVED");
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = "STARTED";

                if (data != null) {
                    LatLng origin = new LatLng(data.getSLatitude(), data.getSLongitude());
                    LatLng providerLat = new LatLng(Double.valueOf(SharedHelper.getKey(activity(), "current_latitude")), Double.valueOf(SharedHelper.getKey(activity(), "current_longitude")));
                    ((MainActivity) Objects.requireNonNull(getActivity())).drawRoute(providerLat, origin, false);
                }

                break;
            case "STARTED":
                btnStatus.setText("ARRIVED");
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = "ARRIVED";
                if (data != null) {
                    LatLng origin = new LatLng(data.getSLatitude(), data.getSLongitude());
                    LatLng providerLat = new LatLng(Double.valueOf(SharedHelper.getKey(activity(), "current_latitude")), Double.valueOf(SharedHelper.getKey(activity(), "current_longitude")));
                    ((MainActivity) Objects.requireNonNull(getActivity())).drawRoute(providerLat, origin, false);
                }
                break;
            case "ARRIVED":
                if (data != null) {
                    ((MainActivity) thisContext).drawDirectionToStop(data.getRouteKey());
                }
                btnStatus.setText("PICKEDUP");
                btnCancel.setVisibility(View.VISIBLE);
                STATUS = "PICKEDUP";
                statusArrivedImg.setImageResource(R.drawable.arrived_select);
                statusPickedUpImg.setImageResource(R.drawable.pickup);
                statusFinishedImg.setImageResource(R.drawable.finished);
                break;
            case "PICKEDUP":
                if (data != null) {
                    ((MainActivity) thisContext).drawDirectionToStop(data.getRouteKey());
                }
                btnStatus.setText("TAP WHEN DROPPED");
                STATUS = "DROPPED";
                statusArrivedImg.setImageResource(R.drawable.arrived);
                statusPickedUpImg.setImageResource(R.drawable.pickup_select);
                statusFinishedImg.setImageResource(R.drawable.finished);
                break;
            default:
                break;
        }
    }


    void confirmPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity());
        alertDialogBuilder
                .setMessage("Are you sure want to drop?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, id) -> statusUpdateCall(STATUS))
                .setNegativeButton("NO", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSuccess(Object object) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(StatusFlowFragment.this).commit();
        Intent intent = new Intent("INTENT_FILTER");
        thisContext.sendBroadcast(intent);
    }


    @Override
    public void onError(Throwable e) {
        hideLoading();
    }


    void statusUpdateCall(String status) {
        if (DATUM != null) {
            Request_ datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", status);
            map.put("_method", "PATCH");
            if (status.equalsIgnoreCase("DROPPED")) {
                map.put("latitude", SharedHelper.getKey(getActivity(), "current_latitude"));
                map.put("longitude", SharedHelper.getKey(getActivity(), "current_longitude"));
                map.put("address", getAddress(new LatLng(Double.parseDouble(SharedHelper.getKey(getActivity(), "current_latitude")), Double.parseDouble(SharedHelper.getKey(activity(), "current_longitude")))));
            }
            presenter.statusUpdate(map, datum.getId());
        }

    }


    void cancelRequestPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisContext);
        alertDialogBuilder
                .setMessage("Are you sure want to Cancel this request?")
                .setCancelable(false)
                .setPositiveButton("YES", (dialog, id) -> {
                    CancelDialogFragment cancelDialogFragment = new CancelDialogFragment();
                    cancelDialogFragment.show(getChildFragmentManager(), cancelDialogFragment.getTag());
                })
                .setNegativeButton("NO", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showOTP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.otp_dialog, null);

        Button submitBtn = view.findViewById(R.id.submit_btn);
        final PinView pinView = view.findViewById(R.id.pinView);

        builder.setView(view);
        otpDialog = builder.create();
        otpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        submitBtn.setOnClickListener(view1 -> {
            if (data.getOtp().equalsIgnoreCase(pinView.getText().toString())) {
                Toast.makeText(thisContext, "OTP Verified!", Toast.LENGTH_SHORT).show();
                statusUpdateCall(STATUS);
                otpDialog.dismiss();
            } else {
                Toast.makeText(thisContext, "Wrong OTP!", Toast.LENGTH_SHORT).show();
            }
        });
        otpDialog.show();
    }

    public String getAddress(LatLng currentLocation) {
        String address = null;
        try {
            Geocoder geocoder = new Geocoder(activity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
            if ((addresses != null) && !addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                    for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(j));
                    }
                } else {
                    strReturnedAddress.append(returnedAddress.getAddressLine(0));
                }
                address = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            Log.e("MAP", "getAddress: " + e);
        }
        return address;
    }

}
