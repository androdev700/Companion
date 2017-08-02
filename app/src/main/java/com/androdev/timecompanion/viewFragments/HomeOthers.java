package com.androdev.timecompanion.viewFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androdev.timecompanion.R;

/**
 * Created by andro on 06/06/17.
 */

public class HomeOthers extends Fragment {

    public HomeOthers() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen_estudy, container, false);
    }

    public static Fragment newInstance() {
        HomeOthers fragment = new HomeOthers();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
