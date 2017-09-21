package com.androdev.timecompanion.days;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androdev.timecompanion.R;

/**
 * Created by ayushsingh on 02/01/17.
 */

public class DayFragment5 extends Fragment {

    public DayFragment5() {
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
        View v = inflater.inflate(R.layout.fragment_day_fragment5, container, false);

        SharedPreferences pref4, room4;
        pref4 = getContext().getSharedPreferences("day5", 0);
        room4 = getContext().getSharedPreferences("class5", 0);

        TextView hour1 = v.findViewById(R.id.d5class1);
        TextView hour2 = v.findViewById(R.id.d5class2);
        TextView hour3 = v.findViewById(R.id.d5class3);
        TextView hour4 = v.findViewById(R.id.d5class4);
        TextView hour5 = v.findViewById(R.id.d5class5);
        TextView hour6 = v.findViewById(R.id.d5class6);
        TextView hour7 = v.findViewById(R.id.d5class7);
        TextView hour8 = v.findViewById(R.id.d5class8);
        TextView hour9 = v.findViewById(R.id.d5class9);
        TextView hour10 = v.findViewById(R.id.d5class10);

        TextView class1 = v.findViewById(R.id.d5room1);
        TextView class2 = v.findViewById(R.id.d5room2);
        TextView class3 = v.findViewById(R.id.d5room3);
        TextView class4 = v.findViewById(R.id.d5room4);
        TextView class5 = v.findViewById(R.id.d5room5);
        TextView class6 = v.findViewById(R.id.d5room6);
        TextView class7 = v.findViewById(R.id.d5room7);
        TextView class8 = v.findViewById(R.id.d5room8);
        TextView class9 = v.findViewById(R.id.d5room9);
        TextView class10 = v.findViewById(R.id.d5room10);

        hour1.setText(pref4.getString("hour1", getString(R.string.free)));
        hour2.setText(pref4.getString("hour2", getString(R.string.free)));
        hour3.setText(pref4.getString("hour3", getString(R.string.free)));
        hour4.setText(pref4.getString("hour4", getString(R.string.free)));
        hour5.setText(pref4.getString("hour5", getString(R.string.free)));
        hour6.setText(pref4.getString("hour6", getString(R.string.free)));
        hour7.setText(pref4.getString("hour7", getString(R.string.free)));
        hour8.setText(pref4.getString("hour8", getString(R.string.free)));
        hour9.setText(pref4.getString("hour9", getString(R.string.free)));
        hour10.setText(pref4.getString("hour10", getString(R.string.free)));

        class1.setText(room4.getString("class1", getString(R.string.free1)));
        class2.setText(room4.getString("class2", getString(R.string.free1)));
        class3.setText(room4.getString("class3", getString(R.string.free1)));
        class4.setText(room4.getString("class4", getString(R.string.free1)));
        class5.setText(room4.getString("class5", getString(R.string.free1)));
        class6.setText(room4.getString("class6", getString(R.string.free1)));
        class7.setText(room4.getString("class7", getString(R.string.free1)));
        class8.setText(room4.getString("class8", getString(R.string.free1)));
        class9.setText(room4.getString("class9", getString(R.string.free1)));
        class10.setText(room4.getString("class10", getString(R.string.free1)));

        //Refine Cards

        if (hour1.getText().equals(getString(R.string.free)) || hour1.getText().equals("") || hour1.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card1);
            cardView.setVisibility(View.GONE);
        }
        if (hour2.getText().equals(getString(R.string.free)) || hour2.getText().equals("") || hour2.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card2);
            cardView.setVisibility(View.GONE);
        }
        if (hour3.getText().equals(getString(R.string.free)) || hour3.getText().equals("") || hour3.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card3);
            cardView.setVisibility(View.GONE);
        }
        if (hour4.getText().equals(getString(R.string.free)) || hour4.getText().equals("") || hour4.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card4);
            cardView.setVisibility(View.GONE);
        }
        if (hour5.getText().equals(getString(R.string.free)) || hour5.getText().equals("") || hour5.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card5);
            cardView.setVisibility(View.GONE);
        }
        if (hour6.getText().equals(getString(R.string.free)) || hour6.getText().equals("") || hour6.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card6);
            cardView.setVisibility(View.GONE);
        }
        if (hour7.getText().equals(getString(R.string.free)) || hour7.getText().equals("") || hour7.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card7);
            cardView.setVisibility(View.GONE);
        }
        if (hour8.getText().equals(getString(R.string.free)) || hour8.getText().equals("") || hour8.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card8);
            cardView.setVisibility(View.GONE);
        }
        if (hour9.getText().equals(getString(R.string.free)) || hour9.getText().equals("") || hour9.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card9);
            cardView.setVisibility(View.GONE);
        }
        if (hour10.getText().equals(getString(R.string.free)) || hour10.getText().equals("") || hour10.getText().equals("Tap to select slot")) {
            CardView cardView = v.findViewById(R.id.d5card10);
            cardView.setVisibility(View.GONE);
        }

        return v;
    }
}
