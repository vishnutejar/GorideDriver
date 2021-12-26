package com.tranxit.enterprise.ui.fragment.upcoming;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseFragment;
import com.tranxit.enterprise.common.SharedHelper;
import com.tranxit.enterprise.common.fcm.MyFirebaseMessagingService;
import com.tranxit.enterprise.data.network.model.HistoryList;
import com.tranxit.enterprise.ui.activity.upcoming_detail.UpcomingTripDetailActivity;
import com.tranxit.enterprise.ui.adapter.UpcomingTripAdapter;
import com.tranxit.enterprise.ui.bottomsheetdialog.cancel.CancelDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class UpcomingTripFragment extends BaseFragment implements UpcomingTripIView, UpcomingTripAdapter.ClickListener {

    @BindView(R.id.upcoming_trip_rv)
    RecyclerView upcomingTripRv;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    Unbinder unbinder;
    List<HistoryList> list = new ArrayList<>();
    UpcomingTripPresenter<UpcomingTripFragment> presenter = new UpcomingTripPresenter<>();
    Context context;

    public UpcomingTripFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upcoming_trip;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);
        getActivity().registerReceiver(myReceiver, new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcomingTripRv.setLayoutManager(mLayoutManager);
        upcomingTripRv.setItemAnimator(new DefaultItemAnimator());
        progressBar.setVisibility(View.VISIBLE);
        presenter.getUpcoming();
        return view;
    }

    @Override
    public void onSuccess(List<HistoryList> historyList) {
        progressBar.setVisibility(View.GONE);
        list.clear();
        list.addAll(historyList);
        loadAdapter();
    }

    @Override
    public void onError(Throwable e) {
        progressBar.setVisibility(View.GONE);
    }

    private void loadAdapter() {
        if (list.size() > 0) {
            UpcomingTripAdapter adapter = new UpcomingTripAdapter(list, context);
            adapter.setClickListener(this);
            upcomingTripRv.setAdapter(adapter);
            upcomingTripRv.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        } else {
            upcomingTripRv.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void redirectClick(HistoryList historyList) {
        Intent intent = new Intent(context, UpcomingTripDetailActivity.class);
        intent.putExtra("request_id", historyList.getId());
        startActivity(intent);
    }

    @Override
    public void cancelRide(HistoryList historyList, int pos) {
        SharedHelper.putKey(getContext(), "cancel_id", String.valueOf(historyList.getId()));
        cancelRequestPopup();
    }


    void cancelRequestPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity());
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

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.getUpcoming();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(myReceiver);
    }
}

