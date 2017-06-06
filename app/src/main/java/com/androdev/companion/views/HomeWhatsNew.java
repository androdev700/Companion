package com.androdev.companion.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androdev.companion.R;

/**
 * Created by andro on 06/06/17.
 */

public class HomeWhatsNew extends Fragment {

    public HomeWhatsNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen_whats_new, container, false);
    }

    public static Fragment newInstance() {
        HomeWhatsNew fragment = new HomeWhatsNew();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
