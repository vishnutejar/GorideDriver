package com.tranxit.enterprise.ui.activity.earnings;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tranxit.enterprise.common.SharedHelper;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.CircularProgressBar;
import com.tranxit.enterprise.common.Constants;
import com.tranxit.enterprise.data.network.model.EarningsList;
import com.tranxit.enterprise.data.network.model.Payment;
import com.tranxit.enterprise.data.network.model.Ride;
import com.tranxit.enterprise.ui.adapter.EarningsTripAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tranxit.enterprise.common.Utilities.animateTextView;


public class EarningsActivity extends BaseActivity implements EarningsIView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.earnings_bar)
    CircularProgressBar earningsBar;
    @BindView(R.id.target_txt)
    TextView targetTxt;
    @BindView(R.id.lblEarnings)
    TextView lblEarnings;
    @BindView(R.id.rides_rv)
    RecyclerView ridesRv;
    @BindView(R.id.error_image)
    ImageView errorImage;
    @BindView(R.id.errorLayout)
    RelativeLayout errorLayout;
    @BindView(R.id.progress_bar)

    ProgressBar progressBar;
    List<Ride> list = new ArrayList<>();
    EarningsTripAdapter adapter;
    EarningsPresenter<EarningsActivity> presenter = new EarningsPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_earnings;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ridesRv.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        ridesRv.setItemAnimator(new DefaultItemAnimator());
        ridesRv.setHasFixedSize(true);

        showLoading();
        presenter.getEarnings();
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
    public void onSuccess(EarningsList earningsLists) {
        hideLoading();
        list.clear();
        list.addAll(earningsLists.getRides());
        loadAdapter(earningsLists);
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }


    private void loadAdapter(EarningsList earningsLists) {
        earningsBar.setProgressWithAnimation(earningsLists.getRidesCount(), 1500);
        animateTextView(0, earningsLists.getRidesCount(), Integer.parseInt(earningsLists.getTarget()), targetTxt);

        double total_price = 0;
        for (Ride ride : earningsLists.getRides()) {
            Payment payment = ride.getPayment();
            if (payment != null) {
                total_price += payment.getProviderPay();
            }
        }
        lblEarnings.setText(String.format("%s%s",
                SharedHelper.getKey(this, SharedHelper.CURRENCY),
                total_price));

        if (list.size() > 0) {
            EarningsTripAdapter adapter = new EarningsTripAdapter(list, activity());
            ridesRv.setAdapter(adapter);
            ridesRv.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        } else {
            ridesRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    }


}
