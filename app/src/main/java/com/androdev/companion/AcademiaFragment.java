package com.androdev.companion;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by ayushsingh on 09/01/17.
 */

public class AcademiaFragment extends Fragment {

    public AcademiaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.academia));

        View v = inflater.inflate(R.layout.fragment_academia, container, false);
        if(checkInternetConnection(inflater, container, savedInstanceState)) {
            WebView webView = (WebView)v.findViewById(R.id.academia_web);
            webView.canGoForward();
            webView.canGoBack();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient() {
                private ProgressDialog mProgress;
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    if (mProgress == null) {
                        mProgress = new ProgressDialog(getContext());
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
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });
            webView.loadUrl("https://academia.srmuniv.ac.in");
        }
        return v;
    }

    private boolean checkInternetConnection(final LayoutInflater inflater, final ViewGroup container,
                                            final Bundle savedInstanceState) {
        ConnectivityManager connect =(ConnectivityManager)getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connect.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            ShowError(inflater, container, savedInstanceState);
            return false;
        }
        return false;
    }

    public static Fragment newInstance() {
        AcademiaFragment fragment = new AcademiaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void ShowError(final LayoutInflater inflater, final ViewGroup container,
                          final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_academia, container, false);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.academia_coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, " Check Your Internet Connection. ",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkInternetConnection(inflater, container, savedInstanceState);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}