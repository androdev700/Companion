package com.androdev.companion;

import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class dayorder2 extends AppCompatActivity {


    TextView sub1;
    TextView sub2;
    TextView sub3;
    TextView sub4;
    TextView sub5;
    TextView sub6;
    TextView sub7;
    TextView sub8;
    TextView sub9;
    TextView sub10;
    TextView hour1;
    TextView hour2;
    TextView hour3;
    TextView hour4;
    TextView hour5;
    TextView hour6;
    TextView hour7;
    TextView hour8;
    TextView hour9;
    TextView hour10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dayorder2);

        sub1 = (TextView) findViewById(R.id.textView22);
        sub2 = (TextView) findViewById(R.id.textView23);
        sub3 = (TextView) findViewById(R.id.textView24);
        sub4 = (TextView) findViewById(R.id.textView25);
        sub5 = (TextView) findViewById(R.id.textView26);
        sub6 = (TextView) findViewById(R.id.textView27);
        sub7 = (TextView) findViewById(R.id.textView28);
        sub8 = (TextView) findViewById(R.id.textView29);
        sub9 = (TextView) findViewById(R.id.textView30);
        sub10 = (TextView) findViewById(R.id.textView31);
        hour1 = (TextView) findViewById(R.id.textView12);
        hour2 = (TextView) findViewById(R.id.textView13);
        hour3 = (TextView) findViewById(R.id.textView14);
        hour4 = (TextView) findViewById(R.id.textView15);
        hour5 = (TextView) findViewById(R.id.textView16);
        hour6 = (TextView) findViewById(R.id.textView17);
        hour7 = (TextView) findViewById(R.id.textView18);
        hour8 = (TextView) findViewById(R.id.textView19);
        hour9 = (TextView) findViewById(R.id.textView20);
        hour10 = (TextView) findViewById(R.id.textView21);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .day2coordinate);

        SharedPreferences class1 = getSharedPreferences("class2", MODE_PRIVATE);
        SharedPreferences pref1 = getSharedPreferences("day2", MODE_PRIVATE);
        sub1.setText(pref1.getString("hour1", "N/A") + " - " + class1.getString("class1", ""));
        sub2.setText(pref1.getString("hour2", "N/A") + " - " + class1.getString("class2", ""));
        sub3.setText(pref1.getString("hour3", "N/A") + " - " + class1.getString("class3", ""));
        sub4.setText(pref1.getString("hour4", "N/A") + " - " + class1.getString("class4", ""));
        sub5.setText(pref1.getString("hour5", "Lunch"));
        sub6.setText(pref1.getString("hour6", "N/A") + " - " + class1.getString("class6", ""));
        sub7.setText(pref1.getString("hour7", "N/A") + " - " + class1.getString("class7", ""));
        sub8.setText(pref1.getString("hour8", "N/A") + " - " + class1.getString("class8", ""));
        sub9.setText(pref1.getString("hour9", "N/A") + " - " + class1.getString("class9", ""));
        sub10.setText(pref1.getString("hour10", "N/A") + " - " + class1.getString("class10", ""));

        hour1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 08:00 - 08:50 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 08:50 - 09:40 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 09:45 - 10:35 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 10:40 - 11:30 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 11:35 - 12:25 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 12:30 - 13:20 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 13:25 - 14:15 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 14:20 - 15:10 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 15:15 - 16:05 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
        hour10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, " 16:05 - 16:55 ", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }
}
