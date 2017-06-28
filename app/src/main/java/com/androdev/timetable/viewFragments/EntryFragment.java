package com.androdev.timetable.viewFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androdev.timetable.MainActivity;
import com.androdev.timetable.R;
import com.androdev.timetable.dayorder.DayOrder;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ayushsingh on 09/01/17.
 */

public class EntryFragment extends Fragment {

    Button button;
    EditText hour1,hour2,hour3,hour4,hour5,hour6,hour7,hour8,hour9,hour10;
    EditText c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;
    Spinner dsp;
    SharedPreferences pref0, pref1, pref2, pref3, pref4, class0, class1, class2, class3, class4;

    public EntryFragment() {
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
        View v = inflater.inflate(R.layout.fragment_entry, container, false);

        hour1 = v.findViewById(R.id.eth1);
        hour2 = v.findViewById(R.id.eth2);
        hour3 = v.findViewById(R.id.eth3);
        hour4 = v.findViewById(R.id.eth4);
        hour5 = v.findViewById(R.id.eth5);
        hour6 = v.findViewById(R.id.eth6);
        hour7 = v.findViewById(R.id.eth7);
        hour8 = v.findViewById(R.id.eth8);
        hour9 = v.findViewById(R.id.eth9);
        hour10 = v.findViewById(R.id.eth10);

        c1 = v.findViewById(R.id.class1);
        c2 = v.findViewById(R.id.class2);
        c3 = v.findViewById(R.id.class3);
        c4 = v.findViewById(R.id.class4);
        c5 = v.findViewById(R.id.class5);
        c6 = v.findViewById(R.id.class6);
        c7 = v.findViewById(R.id.class7);
        c8 = v.findViewById(R.id.class8);
        c9 = v.findViewById(R.id.class9);
        c10 = v.findViewById(R.id.class10);

        dsp = v.findViewById(R.id.dayspinner);
        button = v.findViewById(R.id.save);

        hour1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour3.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour5.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour6.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour7.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour8.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour9.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        hour10.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        final CoordinatorLayout coordinatorLayout = v.findViewById(R.id.enterycoordinate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dsp.setAdapter(adapter);

        pref0 = getActivity().getSharedPreferences("day1", MODE_PRIVATE);
        pref1 = getActivity().getSharedPreferences("day2", MODE_PRIVATE);
        pref2 = getActivity().getSharedPreferences("day3", MODE_PRIVATE);
        pref3 = getActivity().getSharedPreferences("day4", MODE_PRIVATE);
        pref4 = getActivity().getSharedPreferences("day5", MODE_PRIVATE);
        class0 = getActivity().getSharedPreferences("class1", MODE_PRIVATE);
        class1 = getActivity().getSharedPreferences("class2", MODE_PRIVATE);
        class2 = getActivity().getSharedPreferences("class3", MODE_PRIVATE);
        class3 = getActivity().getSharedPreferences("class4", MODE_PRIVATE);
        class4 = getActivity().getSharedPreferences("class5", MODE_PRIVATE);

        AdapterView.OnItemSelectedListener day = new AdapterView.OnItemSelectedListener() {
            String[] days = {"You're now editing Day Order 1", "You're now editing Day Order 2",
                    "You're now editing Day Order 3", "You're now editing Day Order 4",
                    "You're now editing Day Order 5"};

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
                if (position == 0) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, days[position], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hour1.setText(pref0.getString("hour1", null));
                    hour2.setText(pref0.getString("hour2", null));
                    hour3.setText(pref0.getString("hour3", null));
                    hour4.setText(pref0.getString("hour4", null));
                    hour5.setText(pref0.getString("hour5", "Lunch"));
                    hour6.setText(pref0.getString("hour6", null));
                    hour7.setText(pref0.getString("hour7", null));
                    hour8.setText(pref0.getString("hour8", null));
                    hour9.setText(pref0.getString("hour9", null));
                    hour10.setText(pref0.getString("hour10", null));
                    c1.setText(class0.getString("class1", null));
                    c2.setText(class0.getString("class2", null));
                    c3.setText(class0.getString("class3", null));
                    c4.setText(class0.getString("class4", null));
                    c5.setText(class0.getString("class5", null));
                    c6.setText(class0.getString("class6", null));
                    c7.setText(class0.getString("class7", null));
                    c8.setText(class0.getString("class8", null));
                    c9.setText(class0.getString("class9", null));
                    c10.setText(class0.getString("class10", null));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] course = new String[]{hour1.getText().toString(),
                                    hour2.getText().toString(), hour3.getText().toString(),
                                    hour4.getText().toString(), hour5.getText().toString(),
                                    hour6.getText().toString(), hour7.getText().toString(),
                                    hour8.getText().toString(), hour9.getText().toString(),
                                    hour10.getText().toString()};
                            String[] room = new String[]{c1.getText().toString(),
                                    c2.getText().toString(), c3.getText().toString(),
                                    c4.getText().toString(), c5.getText().toString(),
                                    c6.getText().toString(), c7.getText().toString(),
                                    c8.getText().toString(), c9.getText().toString(),
                                    c10.getText().toString()};
                            ArrayList<String> courses = new ArrayList<>(Arrays.asList(course));
                            ArrayList<String> rooms = new ArrayList<>(Arrays.asList(room));
                            DayOrder order = new DayOrder(courses, rooms);
                            MainActivity.writeData("day_order1", order);

                            SharedPreferences.Editor editor0 = pref0.edit();
                            editor0.putString("hour1", hour1.getText().toString());
                            editor0.putString("hour2", hour2.getText().toString());
                            editor0.putString("hour3", hour3.getText().toString());
                            editor0.putString("hour4", hour4.getText().toString());
                            editor0.putString("hour5", hour5.getText().toString());
                            editor0.putString("hour6", hour6.getText().toString());
                            editor0.putString("hour7", hour7.getText().toString());
                            editor0.putString("hour8", hour8.getText().toString());
                            editor0.putString("hour9", hour9.getText().toString());
                            editor0.putString("hour10", hour10.getText().toString());
                            editor0.apply();
                            SharedPreferences.Editor ceditor = class0.edit();
                            ceditor.putString("class1", c1.getText().toString());
                            ceditor.putString("class2", c2.getText().toString());
                            ceditor.putString("class3", c3.getText().toString());
                            ceditor.putString("class4", c4.getText().toString());
                            ceditor.putString("class5", c5.getText().toString());
                            ceditor.putString("class6", c6.getText().toString());
                            ceditor.putString("class7", c7.getText().toString());
                            ceditor.putString("class8", c8.getText().toString());
                            ceditor.putString("class9", c9.getText().toString());
                            ceditor.putString("class10", c10.getText().toString());
                            ceditor.apply();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                } else if (position == 1) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, days[position], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hour1.setText(pref1.getString("hour1", null));
                    hour2.setText(pref1.getString("hour2", null));
                    hour3.setText(pref1.getString("hour3", null));
                    hour4.setText(pref1.getString("hour4", null));
                    hour5.setText(pref1.getString("hour5", "Lunch"));
                    hour6.setText(pref1.getString("hour6", null));
                    hour7.setText(pref1.getString("hour7", null));
                    hour8.setText(pref1.getString("hour8", null));
                    hour9.setText(pref1.getString("hour9", null));
                    hour10.setText(pref1.getString("hour10", null));
                    c1.setText(class1.getString("class1", null));
                    c2.setText(class1.getString("class2", null));
                    c3.setText(class1.getString("class3", null));
                    c4.setText(class1.getString("class4", null));
                    c5.setText(class1.getString("class5", null));
                    c6.setText(class1.getString("class6", null));
                    c7.setText(class1.getString("class7", null));
                    c8.setText(class1.getString("class8", null));
                    c9.setText(class1.getString("class9", null));
                    c10.setText(class1.getString("class10", null));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] course = new String[]{hour1.getText().toString(),
                                    hour2.getText().toString(), hour3.getText().toString(),
                                    hour4.getText().toString(), hour5.getText().toString(),
                                    hour6.getText().toString(), hour7.getText().toString(),
                                    hour8.getText().toString(), hour9.getText().toString(),
                                    hour10.getText().toString()};
                            String[] room = new String[]{c1.getText().toString(),
                                    c2.getText().toString(), c3.getText().toString(),
                                    c4.getText().toString(), c5.getText().toString(),
                                    c6.getText().toString(), c7.getText().toString(),
                                    c8.getText().toString(), c9.getText().toString(),
                                    c10.getText().toString()};
                            ArrayList<String> courses = new ArrayList<>(Arrays.asList(course));
                            ArrayList<String> rooms = new ArrayList<>(Arrays.asList(room));
                            DayOrder order = new DayOrder(courses, rooms);
                            MainActivity.writeData("day_order2", order);

                            SharedPreferences.Editor editor1 = pref1.edit();
                            editor1.putString("hour1", hour1.getText().toString());
                            editor1.putString("hour2", hour2.getText().toString());
                            editor1.putString("hour3", hour3.getText().toString());
                            editor1.putString("hour4", hour4.getText().toString());
                            editor1.putString("hour5", hour5.getText().toString());
                            editor1.putString("hour6", hour6.getText().toString());
                            editor1.putString("hour7", hour7.getText().toString());
                            editor1.putString("hour8", hour8.getText().toString());
                            editor1.putString("hour9", hour9.getText().toString());
                            editor1.putString("hour10", hour10.getText().toString());
                            editor1.apply();
                            SharedPreferences.Editor ceditor1 = class1.edit();
                            ceditor1.putString("class1", c1.getText().toString());
                            ceditor1.putString("class2", c2.getText().toString());
                            ceditor1.putString("class3", c3.getText().toString());
                            ceditor1.putString("class4", c4.getText().toString());
                            ceditor1.putString("class5", c5.getText().toString());
                            ceditor1.putString("class6", c6.getText().toString());
                            ceditor1.putString("class7", c7.getText().toString());
                            ceditor1.putString("class8", c8.getText().toString());
                            ceditor1.putString("class9", c9.getText().toString());
                            ceditor1.putString("class10", c10.getText().toString());
                            ceditor1.apply();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            Fragment fragment = HomeYourTimeTableFragment.newInstance();
                        }
                    });
                } else if (position == 2) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, days[position], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hour1.setText(pref2.getString("hour1", null));
                    hour2.setText(pref2.getString("hour2", null));
                    hour3.setText(pref2.getString("hour3", null));
                    hour4.setText(pref2.getString("hour4", null));
                    hour5.setText(pref2.getString("hour5", "Lunch"));
                    hour6.setText(pref2.getString("hour6", null));
                    hour7.setText(pref2.getString("hour7", null));
                    hour8.setText(pref2.getString("hour8", null));
                    hour9.setText(pref2.getString("hour9", null));
                    hour10.setText(pref2.getString("hour10", null));
                    c1.setText(class2.getString("class1", null));
                    c2.setText(class2.getString("class2", null));
                    c3.setText(class2.getString("class3", null));
                    c4.setText(class2.getString("class4", null));
                    c5.setText(class2.getString("class5", null));
                    c6.setText(class2.getString("class6", null));
                    c7.setText(class2.getString("class7", null));
                    c8.setText(class2.getString("class8", null));
                    c9.setText(class2.getString("class9", null));
                    c10.setText(class2.getString("class10", null));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] course = new String[]{hour1.getText().toString(),
                                    hour2.getText().toString(), hour3.getText().toString(),
                                    hour4.getText().toString(), hour5.getText().toString(),
                                    hour6.getText().toString(), hour7.getText().toString(),
                                    hour8.getText().toString(), hour9.getText().toString(),
                                    hour10.getText().toString()};
                            String[] room = new String[]{c1.getText().toString(),
                                    c2.getText().toString(), c3.getText().toString(),
                                    c4.getText().toString(), c5.getText().toString(),
                                    c6.getText().toString(), c7.getText().toString(),
                                    c8.getText().toString(), c9.getText().toString(),
                                    c10.getText().toString()};
                            ArrayList<String> courses = new ArrayList<>(Arrays.asList(course));
                            ArrayList<String> rooms = new ArrayList<>(Arrays.asList(room));
                            DayOrder order = new DayOrder(courses, rooms);
                            MainActivity.writeData("day_order3", order);

                            SharedPreferences.Editor editor2 = pref2.edit();
                            editor2.putString("hour1", hour1.getText().toString());
                            editor2.putString("hour2", hour2.getText().toString());
                            editor2.putString("hour3", hour3.getText().toString());
                            editor2.putString("hour4", hour4.getText().toString());
                            editor2.putString("hour5", hour5.getText().toString());
                            editor2.putString("hour6", hour6.getText().toString());
                            editor2.putString("hour7", hour7.getText().toString());
                            editor2.putString("hour8", hour8.getText().toString());
                            editor2.putString("hour9", hour9.getText().toString());
                            editor2.putString("hour10", hour10.getText().toString());
                            editor2.apply();
                            SharedPreferences.Editor ceditor2 = class2.edit();
                            ceditor2.putString("class1", c1.getText().toString());
                            ceditor2.putString("class2", c2.getText().toString());
                            ceditor2.putString("class3", c3.getText().toString());
                            ceditor2.putString("class4", c4.getText().toString());
                            ceditor2.putString("class5", c5.getText().toString());
                            ceditor2.putString("class6", c6.getText().toString());
                            ceditor2.putString("class7", c7.getText().toString());
                            ceditor2.putString("class8", c8.getText().toString());
                            ceditor2.putString("class9", c9.getText().toString());
                            ceditor2.putString("class10", c10.getText().toString());
                            ceditor2.apply();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                } else if (position == 3) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, days[position], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hour1.setText(pref3.getString("hour1", null));
                    hour2.setText(pref3.getString("hour2", null));
                    hour3.setText(pref3.getString("hour3", null));
                    hour4.setText(pref3.getString("hour4", null));
                    hour5.setText(pref3.getString("hour5", "Lunch"));
                    hour6.setText(pref3.getString("hour6", null));
                    hour7.setText(pref3.getString("hour7", null));
                    hour8.setText(pref3.getString("hour8", null));
                    hour9.setText(pref3.getString("hour9", null));
                    hour10.setText(pref3.getString("hour10", null));
                    c1.setText(class3.getString("class1", null));
                    c2.setText(class3.getString("class2", null));
                    c3.setText(class3.getString("class3", null));
                    c4.setText(class3.getString("class4", null));
                    c5.setText(class3.getString("class5", null));
                    c6.setText(class3.getString("class6", null));
                    c7.setText(class3.getString("class7", null));
                    c8.setText(class3.getString("class8", null));
                    c9.setText(class3.getString("class9", null));
                    c10.setText(class3.getString("class10", null));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] course = new String[]{hour1.getText().toString(),
                                    hour2.getText().toString(), hour3.getText().toString(),
                                    hour4.getText().toString(), hour5.getText().toString(),
                                    hour6.getText().toString(), hour7.getText().toString(),
                                    hour8.getText().toString(), hour9.getText().toString(),
                                    hour10.getText().toString()};
                            String[] room = new String[]{c1.getText().toString(),
                                    c2.getText().toString(), c3.getText().toString(),
                                    c4.getText().toString(), c5.getText().toString(),
                                    c6.getText().toString(), c7.getText().toString(),
                                    c8.getText().toString(), c9.getText().toString(),
                                    c10.getText().toString()};
                            ArrayList<String> courses = new ArrayList<>(Arrays.asList(course));
                            ArrayList<String> rooms = new ArrayList<>(Arrays.asList(room));
                            DayOrder order = new DayOrder(courses, rooms);
                            MainActivity.writeData("day_order4", order);

                            SharedPreferences.Editor editor3 = pref3.edit();
                            editor3.putString("hour1", hour1.getText().toString());
                            editor3.putString("hour2", hour2.getText().toString());
                            editor3.putString("hour3", hour3.getText().toString());
                            editor3.putString("hour4", hour4.getText().toString());
                            editor3.putString("hour5", hour5.getText().toString());
                            editor3.putString("hour6", hour6.getText().toString());
                            editor3.putString("hour7", hour7.getText().toString());
                            editor3.putString("hour8", hour8.getText().toString());
                            editor3.putString("hour9", hour9.getText().toString());
                            editor3.putString("hour10", hour10.getText().toString());
                            editor3.apply();
                            SharedPreferences.Editor ceditor3 = class3.edit();
                            ceditor3.putString("class1", c1.getText().toString());
                            ceditor3.putString("class2", c2.getText().toString());
                            ceditor3.putString("class3", c3.getText().toString());
                            ceditor3.putString("class4", c4.getText().toString());
                            ceditor3.putString("class5", c5.getText().toString());
                            ceditor3.putString("class6", c6.getText().toString());
                            ceditor3.putString("class7", c7.getText().toString());
                            ceditor3.putString("class8", c8.getText().toString());
                            ceditor3.putString("class9", c9.getText().toString());
                            ceditor3.putString("class10", c10.getText().toString());
                            ceditor3.apply();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                } else if (position == 4) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, days[position], Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    hour1.setText(pref4.getString("hour1", null));
                    hour2.setText(pref4.getString("hour2", null));
                    hour3.setText(pref4.getString("hour3", null));
                    hour4.setText(pref4.getString("hour4", null));
                    hour5.setText(pref4.getString("hour5", "Lunch"));
                    hour6.setText(pref4.getString("hour6", null));
                    hour7.setText(pref4.getString("hour7", null));
                    hour8.setText(pref4.getString("hour8", null));
                    hour9.setText(pref4.getString("hour9", null));
                    hour10.setText(pref4.getString("hour10", null));
                    c1.setText(class4.getString("class1", null));
                    c2.setText(class4.getString("class2", null));
                    c3.setText(class4.getString("class3", null));
                    c4.setText(class4.getString("class4", null));
                    c5.setText(class4.getString("class5", null));
                    c6.setText(class4.getString("class6", null));
                    c7.setText(class4.getString("class7", null));
                    c8.setText(class4.getString("class8", null));
                    c9.setText(class4.getString("class9", null));
                    c10.setText(class4.getString("class10", null));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String[] course = new String[]{hour1.getText().toString(),
                                    hour2.getText().toString(), hour3.getText().toString(),
                                    hour4.getText().toString(), hour5.getText().toString(),
                                    hour6.getText().toString(), hour7.getText().toString(),
                                    hour8.getText().toString(), hour9.getText().toString(),
                                    hour10.getText().toString()};
                            String[] room = new String[]{c1.getText().toString(),
                                    c2.getText().toString(), c3.getText().toString(),
                                    c4.getText().toString(), c5.getText().toString(),
                                    c6.getText().toString(), c7.getText().toString(),
                                    c8.getText().toString(), c9.getText().toString(),
                                    c10.getText().toString()};
                            ArrayList<String> courses = new ArrayList<>(Arrays.asList(course));
                            ArrayList<String> rooms = new ArrayList<>(Arrays.asList(room));
                            DayOrder order = new DayOrder(courses, rooms);
                            MainActivity.writeData("day_order5", order);

                            SharedPreferences.Editor editor4 = pref4.edit();
                            editor4.putString("hour1", hour1.getText().toString());
                            editor4.putString("hour2", hour2.getText().toString());
                            editor4.putString("hour3", hour3.getText().toString());
                            editor4.putString("hour4", hour4.getText().toString());
                            editor4.putString("hour5", hour5.getText().toString());
                            editor4.putString("hour6", hour6.getText().toString());
                            editor4.putString("hour7", hour7.getText().toString());
                            editor4.putString("hour8", hour8.getText().toString());
                            editor4.putString("hour9", hour9.getText().toString());
                            editor4.putString("hour10", hour10.getText().toString());
                            editor4.apply();
                            SharedPreferences.Editor ceditor4 = class4.edit();
                            ceditor4.putString("class1", c1.getText().toString());
                            ceditor4.putString("class2", c2.getText().toString());
                            ceditor4.putString("class3", c3.getText().toString());
                            ceditor4.putString("class4", c4.getText().toString());
                            ceditor4.putString("class5", c5.getText().toString());
                            ceditor4.putString("class6", c6.getText().toString());
                            ceditor4.putString("class7", c7.getText().toString());
                            ceditor4.putString("class8", c8.getText().toString());
                            ceditor4.putString("class9", c9.getText().toString());
                            ceditor4.putString("class10", c10.getText().toString());
                            ceditor4.apply();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Saved",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        };
        dsp.setOnItemSelectedListener(day);
        return v;
    }

    public static Fragment newInstance() {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }
}
