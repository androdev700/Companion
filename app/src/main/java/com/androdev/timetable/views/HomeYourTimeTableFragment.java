package com.androdev.timetable.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androdev.timetable.R;

/**
 * Created by ayushsingh on 02/01/17.
 */

public class HomeYourTimeTableFragment extends Fragment {

    public HomeYourTimeTableFragment() {
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
        return inflater.inflate(R.layout.fragment_home_screen_your_time_table, container, false);
    }

    public static Fragment newInstance() {
        HomeYourTimeTableFragment fragment = new HomeYourTimeTableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}