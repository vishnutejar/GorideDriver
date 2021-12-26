package com.tranxit.enterprise.ui.fragment.offline;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.goride.provider.R;
import com.tranxit.enterprise.base.BaseFragment;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tranxit.enterprise.common.fcm.MyFirebaseMessagingService.INTENT_FILTER;

public class OfflineFragment extends BaseFragment implements OfflineIView {

    OfflinePresenter<OfflineFragment> presenter = new OfflinePresenter<>();
    @BindView(R.id.menu_img)
    ImageView menuImg;
    @BindView(R.id.go_online_btn)
    Button goOnlineBtn;
    Unbinder unbinder;

    DrawerLayout drawer;
    Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_offline;
    }

    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        this.context = getContext();
        presenter.attachView(this);
        drawer = activity().findViewById(R.id.drawer_layout);
        return view;
    }


    @OnClick({R.id.menu_img, R.id.go_online_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_img:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.go_online_btn:
                HashMap<String, Object> map = new HashMap<>();
                map.put("service_status", "active");
                presenter.providerAvailable(map);
                break;
        }
    }

    @Override
    public void onSuccess(Object object) {
        String jsonInString = new Gson().toJson(object);
        try {
            JSONObject jsonObj = new JSONObject(jsonInString);
            if (jsonObj.has("error"))
                Toast.makeText(activity(), jsonObj.optString("error"), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
        Intent intent = new Intent(INTENT_FILTER);
        context.sendBroadcast(intent);
    }

    @Override
    public void onError(Throwable e) {

    }
}
