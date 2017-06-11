package com.androdev.timetable.days;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androdev.timetable.R;

/**
 * Created by ayushsingh on 02/01/17.
 */

public class DayFragment1 extends Fragment {

    public DayFragment1() {
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
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_day_fragment1, container, false);

        SharedPreferences pref0,room0;
        pref0 = getContext().getSharedPreferences("day1",0);
        room0 = getContext().getSharedPreferences("class1",0);

        TextView hour1 = v.findViewById(R.id.d1class1);
        TextView hour2 = v.findViewById(R.id.d1class2);
        TextView hour3 = v.findViewById(R.id.d1class3);
        TextView hour4 = v.findViewById(R.id.d1class4);
        TextView hour5 = v.findViewById(R.id.d1class5);
        TextView hour6 = v.findViewById(R.id.d1class6);
        TextView hour7 = v.findViewById(R.id.d1class7);
        TextView hour8 = v.findViewById(R.id.d1class8);
        TextView hour9 = v.findViewById(R.id.d1class9);
        TextView hour10 = v.findViewById(R.id.d1class10);

        TextView class1 = v.findViewById(R.id.d1room1);
        TextView class2 = v.findViewById(R.id.d1room2);
        TextView class3 = v.findViewById(R.id.d1room3);
        TextView class4 = v.findViewById(R.id.d1room4);
        TextView class5 = v.findViewById(R.id.d1room5);
        TextView class6 = v.findViewById(R.id.d1room6);
        TextView class7 = v.findViewById(R.id.d1room7);
        TextView class8 = v.findViewById(R.id.d1room8);
        TextView class9 = v.findViewById(R.id.d1room9);
        TextView class10 = v.findViewById(R.id.d1room10);

        hour1.setText(pref0.getString("hour1",getString(R.string.free)));
        hour2.setText(pref0.getString("hour2",getString(R.string.free)));
        hour3.setText(pref0.getString("hour3",getString(R.string.free)));
        hour4.setText(pref0.getString("hour4",getString(R.string.free)));
        hour5.setText(pref0.getString("hour5",getString(R.string.free)));
        hour6.setText(pref0.getString("hour6",getString(R.string.free)));
        hour7.setText(pref0.getString("hour7",getString(R.string.free)));
        hour8.setText(pref0.getString("hour8",getString(R.string.free)));
        hour9.setText(pref0.getString("hour9",getString(R.string.free)));
        hour10.setText(pref0.getString("hour10",getString(R.string.free)));

        class1.setText(room0.getString("class1",getString(R.string.free1)));
        class2.setText(room0.getString("class2",getString(R.string.free1)));
        class3.setText(room0.getString("class3",getString(R.string.free1)));
        class4.setText(room0.getString("class4",getString(R.string.free1)));
        class5.setText(room0.getString("class5",getString(R.string.free1)));
        class6.setText(room0.getString("class6",getString(R.string.free1)));
        class7.setText(room0.getString("class7",getString(R.string.free1)));
        class8.setText(room0.getString("class8",getString(R.string.free1)));
        class9.setText(room0.getString("class9",getString(R.string.free1)));
        class10.setText(room0.getString("class10",getString(R.string.free1)));
        return v;
    }

    public static Fragment newInstance() {
        DayFragment1 fragment = new DayFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
}