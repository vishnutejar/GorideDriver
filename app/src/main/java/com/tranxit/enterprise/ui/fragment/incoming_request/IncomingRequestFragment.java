package com.tranxit.enterprise.ui.fragment.incoming_request;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.base.BaseFragment;
import com.tranxit.enterprise.data.network.model.Request_;
import com.tranxit.enterprise.ui.activity.main.MainActivity;

import java.math.BigDecimal;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tranxit.enterprise.base.BaseActivity.DATUM;
import static com.tranxit.enterprise.base.BaseActivity.time_to_left;

public class IncomingRequestFragment extends BaseFragment implements IncomingRequestIView, DirectionCallback {

    Unbinder unbinder;
    @BindView(R.id.btnReject)
    Button btnReject;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.lblCount)
    TextView lblCount;
    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.lblUserName)
    TextView lblUserName;
    @BindView(R.id.ratingUser)
    RatingBar ratingUser;
    @BindView(R.id.pickup_address)
    TextView pickupAddress;
    @BindView(R.id.drop_address)
    TextView dropAddress;
    @BindView(R.id.lblLocationDistance)
    TextView lblLocationDistance;
    @BindView(R.id.pickup_address_layout)
    LinearLayout pickupAddressLayout;
    @BindView(R.id.drop_address_layout)
    LinearLayout dropAddressLayout;

    @BindView(R.id.lblCarType)
    TextView lblCarType;

    Request_ data = null;
    Integer arrivalTime = 0;
    MediaPlayer mPlayer;

    IncomingRequestPresenter<IncomingRequestFragment> presenter = new IncomingRequestPresenter<>();

    Context context;
    CountDownTimer countDownTimer;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_incoming_request;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        mPlayer = MediaPlayer.create(activity(), R.raw.alert_tone);
        presenter.attachView(this);
        init();
        return view;
    }


    void init() {
        data = BaseActivity.DATUM;
        if (data != null) {
            lblUserName.setText(String.format("%s %s", data.getUser().getFirstName(), data.getUser().getLastName()));
            ratingUser.setRating(Float.parseFloat(data.getUser().getRating()));
            pickupAddress.setText(data.getSAddress());
            lblCarType.setText("Car Type: " + data.getServicetype().getName());
            dropAddress.setText(data.getDAddress());
            Glide.with(activity()).load(BuildConfig.BASE_IMAGE_URL + data.getUser().getPicture()).apply(RequestOptions.placeholderOf(R.drawable.user).dontAnimate().error(R.drawable.user)).into(imgUser);
            LatLng providerLat = new LatLng(Double.parseDouble(SharedHelper.getKey(context, "current_latitude")), Double.parseDouble(SharedHelper.getKey(context, "current_longitude")));
            LatLng source = new LatLng(data.getSLatitude(), data.getSLongitude());

            ((MainActivity) context).drawDirectionToStop(data.getRouteKey());

            if (data.getServiceRequired() != null && data.getServiceRequired().equalsIgnoreCase("rental")) {
                dropAddressLayout.setVisibility(View.GONE);
            }

            getDistance(providerLat, source);
            if (!mPlayer.isPlaying())
                mPlayer.start();
        }

        countDownTimer = new CountDownTimer(time_to_left * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                lblCount.setText(String.valueOf(millisUntilFinished / 1000));
                setTvZoomInOutAnimation(lblCount);
            }

            public void onFinish() {
                stopMediaPlayer();
                Intent intent = new Intent("INTENT_FILTER");
                context.sendBroadcast(intent);
            }
        };

        countDownTimer.start();
    }


    public void getDistance(LatLng source, LatLng destination) {
        GoogleDirection.withServerKey(getString(R.string.google_map_key))
                .from(source)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }


    private void setTvZoomInOutAnimation(final TextView textView) {
        final float startSize = 20;
        final float endSize = 13;
        final int animationDuration = 900; // Animation duration in ms

        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);

        animator.addUpdateListener(valueAnimator -> {
            float animatedValue = (Float) valueAnimator.getAnimatedValue();
            textView.setTextSize(animatedValue);
        });

        animator.setRepeatCount(2);
        animator.start();
    }


    @OnClick({R.id.btnReject, R.id.btnAccept})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnReject:
                if (DATUM != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.cancel(datum.getId());
                }
                break;
            case R.id.btnAccept:
                if (DATUM != null && arrivalTime != null) {
                    Request_ datum = DATUM;
                    showLoading();
                    presenter.accept(datum.getId(), arrivalTime);
                }
                break;
        }
    }


    @Override
    public void onSuccessAccept(Object responseBody) {

        countDownTimer.cancel();
        stopMediaPlayer();

        Intent intent = new Intent("INTENT_FILTER");
        context.sendBroadcast(intent);

        hideLoading();
        getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commit();


    }

    @Override
    public void onSuccessCancel(Object object) {
        countDownTimer.cancel();
        stopMediaPlayer();
        hideLoading();
        getActivity().getSupportFragmentManager().beginTransaction().remove(IncomingRequestFragment.this).commit();
        Intent intent = new Intent("INTENT_FILTER");
        context.sendBroadcast(intent);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (isAdded()) {
            if (direction.isOK()) {
                Route route = direction.getRouteList().get(0);
                if (!route.getLegList().isEmpty()) {
                    Leg leg = route.getLegList().get(0);
                    Long duration = Long.valueOf(leg.getDuration().getValue());
                    arrivalTime = (int) (duration / 60);
                    lblLocationDistance.setText(getString(R.string.distance_from, leg.getDistance().getText()) + " | " + arrivalTime);
                }
            } else {
                //Toast.makeText(activity(), direction.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    private String convertSecondToHHMMString(BigDecimal secondTime) {
        long value = secondTime.longValue();
        int hours = (int) value / 3600;
        int minutes = (int) (value % 3600) / 60;
        int seconds = (int) value % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMediaPlayer();
    }

    void stopMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopMediaPlayer();
    }
}
