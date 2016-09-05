package com.androdev.companion;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class academia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academia);
        WebView webView = (WebView) findViewById(R.id.webacademia);

        if (checkInternetConenction()) {
            webView.setWebViewClient(new MyBrowser());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.canGoForward();
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient() {
                private ProgressDialog mProgress;

                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (mProgress == null) {
                        mProgress = new ProgressDialog(academia.this);
                        mProgress.setMessage("Loading...");
                        mProgress.show();
                    }
                    mProgress.setMessage("Loading " + String.valueOf(progress) + "%");
                    if (progress == 100) {
                        mProgress.dismiss();
                        mProgress = null;
                    }
                }
            });
            webView.loadUrl("https://academia.srmuniv.ac.in");
        }
    }
    private boolean checkInternetConenction() {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .academiacoordinate);
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Snackbar snackbar = Snackbar.make(coordinatorLayout, " Check Your Internet Connection. ", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkInternetConenction();
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return false;
        }
        return false;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
