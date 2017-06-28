package com.androdev.timetable;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;

public class SlotSelector extends AppCompatActivity {

    private ArrayList<TextView> courseList;
    private int index;
    private String[] courses;
    private SharedPreferences slotPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_selector);

        courses = new String[]{"courseA", "courseB", "courseC", "courseD", "courseE", "courseF", "courseG"};

        TextView courseA, courseB, courseC, courseD, courseE, courseF, courseG;
        courseA = (TextView) findViewById(R.id.courseA);
        courseB = (TextView) findViewById(R.id.courseB);
        courseC = (TextView) findViewById(R.id.courseC);
        courseD = (TextView) findViewById(R.id.courseD);
        courseE = (TextView) findViewById(R.id.courseE);
        courseF = (TextView) findViewById(R.id.courseF);
        courseG = (TextView) findViewById(R.id.courseG);

        courseList = new ArrayList<>();
        courseList.add(courseA);
        courseList.add(courseB);
        courseList.add(courseC);
        courseList.add(courseD);
        courseList.add(courseE);
        courseList.add(courseF);
        courseList.add(courseG);

        slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        for (int i = 0; i < 7; i++) {
            courseList.get(i).setText(slotPref.getString(courses[i], "Tap to select slot"));
        }
    }

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    public void stopActivity(View view) {
        finish();
    }

    public void slotChoice(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_course_list, null);
        alertDialogBuilder.setView(dialogView);

        switch (view.getId()) {
            case R.id.slotB:
                index = 1;
                break;
            case R.id.slotC:
                index = 2;
                break;
            case R.id.slotD:
                index = 3;
                break;
            case R.id.slotE:
                index = 4;
                break;
            case R.id.slotF:
                index = 5;
                break;
            case R.id.slotG:
                index = 6;
                break;
        }

        final SharedPreferences.Editor editor = slotPref.edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        final AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.course_entry);
        autoCompleteTextView.setAdapter(adapter);
        if (!courseList.get(index).getText().equals("Tap to select slot")) {
            autoCompleteTextView.setText(slotPref.getString(courses[index],""));
        }

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selection = autoCompleteTextView.getText().toString();
                courseList.get(index).setText(selection);
                editor.putString(courses[index], selection);
                editor.apply();
                Log.d("DATA_ENTRY", selection);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
