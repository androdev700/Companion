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

public class DayFragment2 extends Fragment {

    public DayFragment2() {
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
        View v = inflater.inflate(R.layout.fragment_day_fragment2, container, false);

        SharedPreferences pref1,room1;
        pref1 = getContext().getSharedPreferences("day2",0);
        room1 = getContext().getSharedPreferences("class2",0);

        TextView hour1 = (TextView) v.findViewById(R.id.d2class1);
        TextView hour2 = (TextView) v.findViewById(R.id.d2class2);
        TextView hour3 = (TextView) v.findViewById(R.id.d2class3);
        TextView hour4 = (TextView) v.findViewById(R.id.d2class4);
        TextView hour5 = (TextView) v.findViewById(R.id.d2class5);
        TextView hour6 = (TextView) v.findViewById(R.id.d2class6);
        TextView hour7 = (TextView) v.findViewById(R.id.d2class7);
        TextView hour8 = (TextView) v.findViewById(R.id.d2class8);
        TextView hour9 = (TextView) v.findViewById(R.id.d2class9);
        TextView hour10 = (TextView) v.findViewById(R.id.d2class10);

        TextView class1 = (TextView) v.findViewById(R.id.d2room1);
        TextView class2 = (TextView) v.findViewById(R.id.d2room2);
        TextView class3 = (TextView) v.findViewById(R.id.d2room3);
        TextView class4 = (TextView) v.findViewById(R.id.d2room4);
        TextView class5 = (TextView) v.findViewById(R.id.d2room5);
        TextView class6 = (TextView) v.findViewById(R.id.d2room6);
        TextView class7 = (TextView) v.findViewById(R.id.d2room7);
        TextView class8 = (TextView) v.findViewById(R.id.d2room8);
        TextView class9 = (TextView) v.findViewById(R.id.d2room9);
        TextView class10 = (TextView) v.findViewById(R.id.d2room10);

        hour1.setText(pref1.getString("hour1",getString(R.string.free)));
        hour2.setText(pref1.getString("hour2",getString(R.string.free)));
        hour3.setText(pref1.getString("hour3",getString(R.string.free)));
        hour4.setText(pref1.getString("hour4",getString(R.string.free)));
        hour5.setText(pref1.getString("hour5",getString(R.string.free)));
        hour6.setText(pref1.getString("hour6",getString(R.string.free)));
        hour7.setText(pref1.getString("hour7",getString(R.string.free)));
        hour8.setText(pref1.getString("hour8",getString(R.string.free)));
        hour9.setText(pref1.getString("hour9",getString(R.string.free)));
        hour10.setText(pref1.getString("hour10",getString(R.string.free)));

        class1.setText(room1.getString("class1",getString(R.string.free1)));
        class2.setText(room1.getString("class2",getString(R.string.free1)));
        class3.setText(room1.getString("class3",getString(R.string.free1)));
        class4.setText(room1.getString("class4",getString(R.string.free1)));
        class5.setText(room1.getString("class5",getString(R.string.free1)));
        class6.setText(room1.getString("class6",getString(R.string.free1)));
        class7.setText(room1.getString("class7",getString(R.string.free1)));
        class8.setText(room1.getString("class8",getString(R.string.free1)));
        class9.setText(room1.getString("class9",getString(R.string.free1)));
        class10.setText(room1.getString("class10",getString(R.string.free1)));
        return v;
    }

    public static Fragment newInstance() {
        DayFragment2 fragment = new DayFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
}
