package com.tranxit.enterprise.ui.activity.photoview;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView image;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_view;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String url = extras.getString("url");
            String name = extras.getString("name", "");
            Glide.with(this).load(BuildConfig.BASE_IMAGE_URL + url).apply(RequestOptions.placeholderOf(R.drawable.ic_photo_camera).dontAnimate().error(R.drawable.ic_photo_camera)).into(image);
            this.setTitle(name);

        }
    }

}
