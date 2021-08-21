package com.tranxit.enterprise.ui.activity.aboutus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.tranxit.enterprise.driver.BuildConfig;
import com.tranxit.enterprise.driver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.privacy)
    Button privacy;
    @BindView(R.id.terms)
    Button terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        webView.loadUrl(BuildConfig.BASE_URL + "about_us");
    }

    @OnClick({R.id.privacy, R.id.terms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.privacy:
                loadURL(BuildConfig.BASE_URL + "privacy");
                break;
            case R.id.terms:
                loadURL(BuildConfig.BASE_URL + "terms");
                break;
        }
    }

    void loadURL(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
