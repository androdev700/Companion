package com.androdev.companion;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ayushsingh on 02/01/17.
 */

public class DayFragment4 extends Fragment {

    public DayFragment4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.dayorder4));

        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_day_fragment4, container, false);

        SharedPreferences pref3,room3;
        pref3 = getContext().getSharedPreferences("day4",0);
        room3 = getContext().getSharedPreferences("class4",0);

        TextView hour1 = (TextView) v.findViewById(R.id.d4class1);
        TextView hour2 = (TextView) v.findViewById(R.id.d4class2);
        TextView hour3 = (TextView) v.findViewById(R.id.d4class3);
        TextView hour4 = (TextView) v.findViewById(R.id.d4class4);
        TextView hour5 = (TextView) v.findViewById(R.id.d4class5);
        TextView hour6 = (TextView) v.findViewById(R.id.d4class6);
        TextView hour7 = (TextView) v.findViewById(R.id.d4class7);
        TextView hour8 = (TextView) v.findViewById(R.id.d4class8);
        TextView hour9 = (TextView) v.findViewById(R.id.d4class9);
        TextView hour10 = (TextView) v.findViewById(R.id.d4class10);

        TextView class1 = (TextView) v.findViewById(R.id.d4room1);
        TextView class2 = (TextView) v.findViewById(R.id.d4room2);
        TextView class3 = (TextView) v.findViewById(R.id.d4room3);
        TextView class4 = (TextView) v.findViewById(R.id.d4room4);
        TextView class5 = (TextView) v.findViewById(R.id.d4room5);
        TextView class6 = (TextView) v.findViewById(R.id.d4room6);
        TextView class7 = (TextView) v.findViewById(R.id.d4room7);
        TextView class8 = (TextView) v.findViewById(R.id.d4room8);
        TextView class9 = (TextView) v.findViewById(R.id.d4room9);
        TextView class10 = (TextView) v.findViewById(R.id.d4room10);

        hour1.setText(pref3.getString("hour1",getString(R.string.free)));
        hour2.setText(pref3.getString("hour2",getString(R.string.free)));
        hour3.setText(pref3.getString("hour3",getString(R.string.free)));
        hour4.setText(pref3.getString("hour4",getString(R.string.free)));
        hour5.setText(pref3.getString("hour5",getString(R.string.free)));
        hour6.setText(pref3.getString("hour6",getString(R.string.free)));
        hour7.setText(pref3.getString("hour7",getString(R.string.free)));
        hour8.setText(pref3.getString("hour8",getString(R.string.free)));
        hour9.setText(pref3.getString("hour9",getString(R.string.free)));
        hour10.setText(pref3.getString("hour10",getString(R.string.free)));

        class1.setText(room3.getString("class1",getString(R.string.free1)));
        class2.setText(room3.getString("class2",getString(R.string.free1)));
        class3.setText(room3.getString("class3",getString(R.string.free1)));
        class4.setText(room3.getString("class4",getString(R.string.free1)));
        class5.setText(room3.getString("class5",getString(R.string.free1)));
        class6.setText(room3.getString("class6",getString(R.string.free1)));
        class7.setText(room3.getString("class7",getString(R.string.free1)));
        class8.setText(room3.getString("class8",getString(R.string.free1)));
        class9.setText(room3.getString("class9",getString(R.string.free1)));
        class10.setText(room3.getString("class10",getString(R.string.free1)));
        
        return v;
    }

    public static Fragment newInstance() {
        DayFragment4 fragment = new DayFragment4();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit) {
            Fragment fragment = EntryFragment.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left,R.anim.exit_to_right)
                    .replace(R.id.main_frag, fragment)
                    .addToBackStack("null")
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
