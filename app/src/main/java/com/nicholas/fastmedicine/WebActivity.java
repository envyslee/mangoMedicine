package com.nicholas.fastmedicine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    private WebView my_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        my_webview = (WebView) findViewById(R.id.my_webview);
        WebSettings webSettings = my_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        my_webview.loadUrl("http://wap.vcash.cn");
        my_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//阻止跳到其他浏览器
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (my_webview.canGoBack()) {
            my_webview.goBack();
        } else {
            finish();
        }
    }
}
