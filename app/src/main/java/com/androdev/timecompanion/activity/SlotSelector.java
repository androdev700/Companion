package com.androdev.timecompanion.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androdev.timecompanion.R;
import com.androdev.timecompanion.dayorder.DayOrder;
import com.androdev.timecompanion.labslot.LabSlot;
import com.androdev.timecompanion.slot.Slot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SlotSelector extends AppCompatActivity {

    private ArrayList<TextView> courseList;
    private ArrayList<TextView> courseLabList;
    private int index;
    private int indexLab;
    private String[] courses;
    private String[] labCourses;
    private SharedPreferences slotPref;
    private SharedPreferences slotRoomPref;
    private SharedPreferences labTime;
    private SharedPreferences pref0, pref1, pref2, pref3, pref4, class0, class1, class2, class3, class4;
    private ArrayList<SharedPreferences> coursePrefList;
    private ArrayList<SharedPreferences> courseClassPrefList;
    private SharedPreferences batchPref;
    private SharedPreferences.Editor edit;
    private SharedPreferences.Editor venueEdit;
    private String[] hourName = new String[]{"hour1", "hour2", "hour3", "hour4", "hour5", "hour6",
            "hour7", "hour8", "hour9", "hour10"};
    private String[] dayName = new String[]{"day_order1", "day_order2", "day_order3", "day_order4", "day_order5"};
    private SharedPreferences prefDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_selector);

        TextView courseA, courseB, courseC, courseD, courseE, courseF, courseG, courseLab1, courseLab2, courseLab3, courseLab4;
        courseA = findViewById(R.id.courseA);
        courseB = findViewById(R.id.courseB);
        courseC = findViewById(R.id.courseC);
        courseD = findViewById(R.id.courseD);
        courseE = findViewById(R.id.courseE);
        courseF = findViewById(R.id.courseF);
        courseG = findViewById(R.id.courseG);
        courseLab1 = findViewById(R.id.courseLab1);
        courseLab2 = findViewById(R.id.courseLab2);
        courseLab3 = findViewById(R.id.courseLab3);
        courseLab4 = findViewById(R.id.courseLab4);

        courseList = new ArrayList<>();
        courseList.add(courseA);
        courseList.add(courseB);
        courseList.add(courseC);
        courseList.add(courseD);
        courseList.add(courseE);
        courseList.add(courseF);
        courseList.add(courseG);

        courseLabList = new ArrayList<>();
        courseLabList.add(courseLab1);
        courseLabList.add(courseLab2);
        courseLabList.add(courseLab3);
        courseLabList.add(courseLab4);
        prefDisplayName = getSharedPreferences("account_info", MODE_PRIVATE);
        startDatabase();
    }

    public void deleteUserData(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        Query userData = databaseReference.child(prefDisplayName.getString("uid", ""));
                        userData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot e : dataSnapshot.getChildren()) {
                                    e.getRef().removeValue();
                                    removeUserData();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("SLOT_SELECTOR", "onCancelled", databaseError.toException());
                            }
                        });
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This will delete all your data. Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    public void removeUserData() {
        pref0.edit().clear().apply();
        pref1.edit().clear().apply();
        pref2.edit().clear().apply();
        pref3.edit().clear().apply();
        pref4.edit().clear().apply();
        class0.edit().clear().apply();
        class1.edit().clear().apply();
        class2.edit().clear().apply();
        class3.edit().clear().apply();
        class4.edit().clear().apply();
        SharedPreferences slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        SharedPreferences slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
        SharedPreferences labTime = getSharedPreferences("LabTime", MODE_PRIVATE);
        slotPref.edit().clear().apply();
        slotRoomPref.edit().clear().apply();
        labTime.edit().clear().apply();
        getSharedPreferences("batch", MODE_PRIVATE).edit().clear().apply();
        Toast.makeText(getBaseContext(), "All data cleared..", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void startDatabase() {
        Arrays.sort(COURSES);
        courses = new String[]{"courseA", "courseB", "courseC", "courseD", "courseE", "courseF", "courseG"};
        labCourses = new String[]{"courseLab1", "courseLab2", "courseLab3", "courseLab4"};

        slotPref = getSharedPreferences("SlotChoice", MODE_PRIVATE);
        for (int i = 0; i < 7; i++) {
            courseList.get(i).setText(slotPref.getString(courses[i], "Tap to select slot"));
        }
        for (int i = 0; i < 4; i++) {
            courseLabList.get(i).setText(slotPref.getString(labCourses[i], "Tap to select slot"));
        }
        slotRoomPref = getSharedPreferences("SlotRoom", MODE_PRIVATE);
        labTime = getSharedPreferences("LabTime", MODE_PRIVATE);

        pref0 = getSharedPreferences("day1", MODE_PRIVATE);
        pref1 = getSharedPreferences("day2", MODE_PRIVATE);
        pref2 = getSharedPreferences("day3", MODE_PRIVATE);
        pref3 = getSharedPreferences("day4", MODE_PRIVATE);
        pref4 = getSharedPreferences("day5", MODE_PRIVATE);
        class0 = getSharedPreferences("class1", MODE_PRIVATE);
        class1 = getSharedPreferences("class2", MODE_PRIVATE);
        class2 = getSharedPreferences("class3", MODE_PRIVATE);
        class3 = getSharedPreferences("class4", MODE_PRIVATE);
        class4 = getSharedPreferences("class5", MODE_PRIVATE);

        coursePrefList = new ArrayList<>();
        coursePrefList.add(pref0);
        coursePrefList.add(pref1);
        coursePrefList.add(pref2);
        coursePrefList.add(pref3);
        coursePrefList.add(pref4);

        courseClassPrefList = new ArrayList<>();
        courseClassPrefList.add(class0);
        courseClassPrefList.add(class1);
        courseClassPrefList.add(class2);
        courseClassPrefList.add(class3);
        courseClassPrefList.add(class4);

        batchPref = getSharedPreferences("batch", MODE_PRIVATE);
    }

    public void stopActivity(View view) {
        writeData();
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Log.d("TAG", "Back Pressed");
        writeData();
    }

    public void writeData() {
        ArrayList<String> course = new ArrayList<>();
        ArrayList<String> room = new ArrayList<>();
        ArrayList<String> courseLab = new ArrayList<>();
        ArrayList<String> roomLab = new ArrayList<>();
        ArrayList<String> timeLab = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            course.add(slotPref.getString(courses[i], "Tap to select slot"));
            room.add(slotRoomPref.getString(courses[i], ""));
        }
        for (int i = 0; i < 4; i++) {
            courseLab.add(slotPref.getString(labCourses[i], "Tap to select slot"));
            timeLab.add(labTime.getString(labCourses[i], ""));
            roomLab.add(slotRoomPref.getString(labCourses[i], ""));
        }
        Slot slot = new Slot(course, room);
        LabSlot labSlot = new LabSlot(courseLab, roomLab, timeLab);
        MainActivity.writeDataSlot(slot);
        MainActivity.writeLabSlot(labSlot);

        for (int i = 0; i < 5; i++) {
            String courseDay[] = new String[10];
            SharedPreferences pushDay = coursePrefList.get(i);
            Log.d(dayName[i], "--------------");
            for (int j = 0; j < 10; j++) {
                String data = pushDay.getString(hourName[j], "");
                if (data.isEmpty()) {
                    courseDay[j] = "";
                }
                courseDay[j] = data;
                Log.d(hourName[j], courseDay[j]);
            }
            ArrayList<String> courseData = new ArrayList<>(Arrays.asList(courseDay));
            DayOrder order = new DayOrder(courseData);
            MainActivity.writeData(dayName[i], order);
        }
    }

    public void slotChoice(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_course_list, null);
        alertDialogBuilder.setView(dialogView);

        switch (view.getId()) {
            case R.id.slotA:
                index = 0;
                alertDialogBuilder.setTitle("Enter Slot A");
                break;
            case R.id.slotB:
                index = 1;
                alertDialogBuilder.setTitle("Enter Slot B");
                break;
            case R.id.slotC:
                index = 2;
                alertDialogBuilder.setTitle("Enter Slot C");
                break;
            case R.id.slotD:
                index = 3;
                alertDialogBuilder.setTitle("Enter Slot D");
                break;
            case R.id.slotE:
                index = 4;
                alertDialogBuilder.setTitle("Enter Slot E");
                break;
            case R.id.slotF:
                index = 5;
                alertDialogBuilder.setTitle("Enter Slot F");
                break;
            case R.id.slotG:
                index = 6;
                alertDialogBuilder.setTitle("Enter Slot G");
                break;
        }

        final SharedPreferences.Editor editor = slotPref.edit();
        final SharedPreferences.Editor roomEditor = slotRoomPref.edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COURSES);
        final AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.course_entry);
        autoCompleteTextView.setAdapter(adapter);
        if (!courseList.get(index).getText().equals("Tap to select slot")) {
            autoCompleteTextView.setText(slotPref.getString(courses[index], ""));
        }
        final EditText editText = dialogView.findViewById(R.id.course_room);
        editText.setText(slotRoomPref.getString(courses[index], ""));

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selection = autoCompleteTextView.getText().toString();
                courseList.get(index).setText(selection);
                editor.putString(courses[index], selection);
                editor.apply();
                Log.d("DATA_ENTRY", selection);

                String roomSelection = editText.getText().toString();
                roomEditor.putString(courses[index], roomSelection);
                roomEditor.apply();
                Log.d("DATA_ENTRY", roomSelection);

                if (batchPref.getString("batch", "Error").equals("1")) {
                    switch (index) {
                        case 0:
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour1", slotPref.getString(courses[index], ""));
                            edit.putString("hour2", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour10", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour3", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 1:
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour6", slotPref.getString(courses[index], ""));
                            edit.putString("hour7", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour8", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour4", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 2:
                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour1", slotPref.getString(courses[index], ""));
                            edit.putString("hour2", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour9", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour5", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 3:
                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour6", slotPref.getString(courses[index], ""));
                            edit.putString("hour7", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour5", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour5", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 4:
                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour1", slotPref.getString(courses[index], ""));
                            edit.putString("hour2", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour3", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 5:
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour3", slotPref.getString(courses[index], ""));
                            edit.putString("hour4", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour4", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 6:
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour8", slotPref.getString(courses[index], ""));
                            edit.putString("hour9", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour10", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                    }
                } else if (batchPref.getString("batch", "Error").equals("2")) {
                    switch (index) {
                        case 0:
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour6", slotPref.getString(courses[index], ""));
                            edit.putString("hour7", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour5", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour8", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 1:
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour1", slotPref.getString(courses[index], ""));
                            edit.putString("hour2", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour3", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour9", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 2:
                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour6", slotPref.getString(courses[index], ""));
                            edit.putString("hour7", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour4", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour10", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 3:
                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour1", slotPref.getString(courses[index], ""));
                            edit.putString("hour2", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class1", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class2", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour10", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour10", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class10", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 4:
                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                            edit.putString("hour6", slotPref.getString(courses[index], ""));
                            edit.putString("hour7", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class6", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class7", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour8", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 5:
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                            edit.putString("hour8", slotPref.getString(courses[index], ""));
                            edit.putString("hour9", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class8", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                            edit.putString("hour9", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class9", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                        case 6:
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                            edit.putString("hour3", slotPref.getString(courses[index], ""));
                            edit.putString("hour4", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class3", slotRoomPref.getString(courses[index], ""));
                            venueEdit.putString("class4", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();

                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                            edit.putString("hour5", slotPref.getString(courses[index], ""));
                            venueEdit.putString("class5", slotRoomPref.getString(courses[index], ""));
                            edit.apply();
                            venueEdit.apply();
                            break;
                    }
                }

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

    public void slotChoiceLab(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_lab_list, null);
        alertDialogBuilder.setView(dialogView);

        switch (view.getId()) {
            case R.id.slotLab1:
                indexLab = 0;
                alertDialogBuilder.setTitle("Enter Lab 1");
                break;
            case R.id.slotLab2:
                alertDialogBuilder.setTitle("Enter Lab 2");
                indexLab = 1;
                break;
            case R.id.slotLab3:
                alertDialogBuilder.setTitle("Enter Lab 3");
                indexLab = 2;
                break;
            case R.id.slotLab4:
                alertDialogBuilder.setTitle("Enter Lab 4");
                indexLab = 3;
                break;
        }

        final SharedPreferences.Editor editor = slotPref.edit();
        final SharedPreferences.Editor roomEditor = slotRoomPref.edit();
        final SharedPreferences.Editor labTimeEditor = labTime.edit();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COURSES);
        final AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.lab_entry);
        autoCompleteTextView.setAdapter(adapter);
        if (!courseLabList.get(indexLab).getText().equals("Tap to select slot")) {
            autoCompleteTextView.setText(slotPref.getString(labCourses[indexLab], ""));
        }
        final EditText editText = dialogView.findViewById(R.id.lab_room);
        editText.setText(slotRoomPref.getString(labCourses[indexLab], ""));

        final EditText start = dialogView.findViewById(R.id.lab_hour_entry_start);
        final EditText end = dialogView.findViewById(R.id.lab_hour_entry_end);
        String info[];
        if (!((labTime.getString(labCourses[indexLab], "")).equals(""))) {
            info = labTime.getString(labCourses[indexLab], "").split(" ");
            start.setText(info[0]);
            end.setText(info[1]);
        }

        start.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (start.getText().length() == 2) {
                    end.requestFocus();
                }
                return false;
            }
        });

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selection = autoCompleteTextView.getText().toString();
                courseLabList.get(indexLab).setText(selection);
                editor.putString(labCourses[indexLab], selection);
                editor.apply();
                Log.d("DATA_ENTRY", selection);

                String roomSelection = editText.getText().toString();
                roomEditor.putString(labCourses[indexLab], roomSelection);
                roomEditor.apply();
                Log.d("DATA_ENTRY", roomSelection);
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selection = autoCompleteTextView.getText().toString();
                courseLabList.get(indexLab).setText(selection);
                editor.putString(labCourses[indexLab], selection);
                editor.apply();
                Log.d("DATA_ENTRY", selection);

                String roomSelection = editText.getText().toString();
                roomEditor.putString(labCourses[indexLab], roomSelection);
                roomEditor.apply();
                Log.d("DATA_ENTRY", roomSelection);
                int startTime = Integer.parseInt(start.getText().toString());
                int endTime = Integer.parseInt(end.getText().toString());
                if (endTime - startTime > 4) {
                    Toast.makeText(getBaseContext(), "Cannot set length more than 5 hours, Try Again", Toast.LENGTH_SHORT).show();
                } else if (endTime < 0 || endTime > 50) {
                    Toast.makeText(getBaseContext(), "Cannot set length more than 50, Try Again", Toast.LENGTH_SHORT).show();
                } else if (startTime < 0 || startTime > 50) {
                    Toast.makeText(getBaseContext(), "Cannot set length more than 50, Try Again", Toast.LENGTH_SHORT).show();
                } else if (startTime > endTime) {
                    Toast.makeText(getBaseContext(), "Start cannot exceed end, Try Again", Toast.LENGTH_SHORT).show();
                } else if (!batchCheck(startTime, endTime)) {
                    Toast.makeText(getBaseContext(), "Your batch doesn't allow this lab timing", Toast.LENGTH_SHORT).show();
                } else {
                    String data = start.getText().toString().concat(" ").concat(end.getText().toString());
                    labTimeEditor.putString(labCourses[indexLab], data);
                    labTimeEditor.apply();
                    Log.d("DATA_ENTRY", data);
                    alertDialog.dismiss();

                    if (batchPref.getString("batch", "Error").equals("1")) {
                        if (endTime < 11) {
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                        } else if (endTime < 16) {
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                        } else if (endTime < 31) {
                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                        } else if (endTime < 36) {
                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                        } else if (endTime < 51) {
                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                        }
                        String s = Integer.toString(startTime % 10);

                        while (startTime <= endTime) {
                            Log.d("TAG", "hour".concat(s));
                            edit.putString("hour".concat(s), slotPref.getString(labCourses[indexLab], ""));
                            venueEdit.putString("class".concat(s), slotRoomPref.getString(labCourses[indexLab], ""));
                            startTime++;
                            int n = Integer.parseInt(s);
                            s = Integer.toString(++n);
                        }
                        edit.apply();
                        venueEdit.apply();

                    } else if (batchPref.getString("batch", "Error").equals("2")) {
                        if (endTime < 6) {
                            edit = coursePrefList.get(0).edit();
                            venueEdit = courseClassPrefList.get(0).edit();
                        } else if (endTime < 21) {
                            edit = coursePrefList.get(1).edit();
                            venueEdit = courseClassPrefList.get(1).edit();
                        } else if (endTime < 26) {
                            edit = coursePrefList.get(2).edit();
                            venueEdit = courseClassPrefList.get(2).edit();
                        } else if (endTime < 41) {
                            edit = coursePrefList.get(3).edit();
                            venueEdit = courseClassPrefList.get(3).edit();
                        } else if (endTime < 46) {
                            edit = coursePrefList.get(4).edit();
                            venueEdit = courseClassPrefList.get(4).edit();
                        }
                        String s = Integer.toString(startTime % 10);

                        while (startTime <= endTime) {
                            Log.d("TAG", "hour".concat(s));
                            edit.putString("hour".concat(s), slotPref.getString(labCourses[indexLab], ""));
                            venueEdit.putString("class".concat(s), slotRoomPref.getString(labCourses[indexLab], ""));
                            startTime++;
                            int n = Integer.parseInt(s);
                            s = Integer.toString(++n);
                        }
                        edit.apply();
                        venueEdit.apply();
                    }

                }
            }
        });
    }

    public boolean batchCheck(int start, int end) {
        boolean flag = false;
        if (batchPref.getString("batch", "Error").equals("1")) {
            if (start > 5 && end < 11) {
                flag = true;
            } else if (start > 10 && end < 16) {
                flag = true;
            } else if (start > 25 && end < 31) {
                flag = true;
            } else if (start > 30 && end < 36) {
                flag = true;
            } else if (start > 45 && end < 51) {
                flag = true;
            }
        } else if (batchPref.getString("batch", "Error").equals("2")) {
            if (start > 0 && end < 6) {
                flag = true;
            } else if (start > 15 && end < 21) {
                flag = true;
            } else if (start > 20 && end < 26) {
                flag = true;
            } else if (start > 35 && end < 41) {
                flag = true;
            } else if (start > 40 && end < 46) {
                flag = true;
            }
        }
        return flag;
    }

    private static final String[] COURSES = new String[]{

            //Information Technology

            "Program Design And Development Laboratory",
            "Computer Organisation And Architecture",
            "IT Fundamentals",
            "Professional Ethics",
            "Database Management Systems",
            "Computer Networks",
            "System Integration And Architecture",
            "Principles of Operating Systems",
            "Web Programming",
            "Network Protocols And Programming",
            "Human Computer Interaction",
            "Integrative Programming And Technology",
            "Information Assurance And Security",
            "IT Infrastructure Management",
            "Multi Disciplinary Design",
            "Python Programming",
            "Game Programming",
            "Mobile Application Development",
            "Cloud Computing",
            "Cryptography",
            "Parallel Programming Using OpenCL",
            "Database Administration",
            "Text Mining",
            "Computer Graphics",
            "Software Testing",
            "Data Warehousing And Data Mining",
            "Enterprise Resource Planning",
            "Management Information Systems",
            "Multimedia Tools And Applications",
            "Digital Audio And Computer Music",
            "Linux Internals",
            "Computer Animation: Algorithms And Techniques",
            "Data Compression",
            "Engineering Economics And Financial Management",
            "Advanced Java Programming And Technology",
            "Linux Administration",
            "Fundamentals Of Virtualization",
            "Internet Security And Computer Forensics",
            "Information Storage and Management",
            "Information And Network Security",
            "Internet Of Things",
            "Data Science And Big Data Analytics",
            "Business Intelligence And Analytics",
            "Parallel Architecture And Algorithms",
            "Forensics And Incident Response",
            "Multilayer Switching",
            "Network Simulation And Modelling",
            "Interactive Web Page Scripting",
            "Programming Multimedia For The Web",
            "Advanced Web Application Development",
            "Cloud Application Development",
            "Information Security",
            "Introduction To Database Management Systems",
            "Fundamentals of Big Data Analytics",
            "Fundamentals of Cloud Computing",
            "Computer Networking",
            "Statistics For Information Technology",

            //Software

            "Object Oriented Programming Using C++",
            "Software Engineering Principles",
            "Object Oriented Analysis And Design",
            "Professional Ethics And Software Economics",
            "Requirements Engineering",
            "Software Architecture And Design",
            "Software Modelling And Analysis",
            "Software Project Management",
            "Software Verification And Validation",
            "Security In Networks And Software Development",
            "Principles Of Programming Languages",
            "E-Commerce",
            "Design Patterns",
            "Multimedia Systems",
            "System Software",
            "Principles Of Compiler Design",
            "Distributed Operating System",
            "Programming in PHP",
            "Visual Programming",
            "Agile Software Process",
            "Xml And Web Services",
            "Pervasive Computing",
            "Advanced Java Programming",
            "Software Reliability",
            "Software Quality Assurance",
            "Software Configuration Management",
            "Analysis Of Software Artifacts",
            "Software Maintenance And Administration",
            "Software Measurements And Metrics",

            //Fetched from Academia
            "Employability Skills",
            "Basic Civil Engineering",
            "Programming Laboratory",
            "Algorithm Analysis And Design",
            "Microprocessor And Microcontrollers",
            "Chemistry",
            "Chemistry Laboratory",
            "Principles of Environmental Science",
            "Basic Electronics Engineering",
            "Basic Electrical Engineering",
            "Computer Hardware and Troubleshooting Laboratory",
            "Program Design and Development",
            "Value Education | Soft Skills",
            "English | Soft Skills",
            "German Language",
            "French Language",
            "Japanese Language",
            "Chinese Language",
            "Korean Language",
            "Advanced Calculus and Complex Analysis",
            "Calculus and Solid Geometry",
            "Discrete Mathematics for Information Technology",
            "Basic Mechanical Engineering",
            "Engineering Graphics",
            "Quantitative Aptitude and Logical Reasoning",
            "Communication and Reasoning Skills",
            "Verbal Aptitude",
            "Physics",
            "Physics Laboratory",
            "Material Science",
            "Programming in JAVA",
            "Data Structures",
            "Principles of Communication System",
            "Probability and Queuing Theory",
            "Neuro Fuzzy and Genetic Programming",
            "Distributed Computing",
            "Machine Learning",
            "Wireless Sensor Networks",
            "Knowledge Based Decision Support Systems",
            "Software Defined Networks",
            "Semantic Web",
            "Network Design And Management",
            "Wireless And Mobile Communication",
            "Non Destructive Testing Methods",
            "Composite Materials And Structures",
            "Auxiliary Vehicle Systems",
            "Design For Safety And Comfort",
            "Electric And Hybrid Vehicles",
            "Air Transportation And Aircraft Maintenance Management",
            "Aircraft General Engineering and Maintenance Practices",
            "Applied Structural Mechanics",
            "Fundamentals Of Combustion",
            "Applied Optoelectronics In Medicine",
            "Dental Engineering",
            "Medical Simulation in Life Supporting Devices",
            "Hospital Information System",
            "Biomedical Nano Technology",
            "Biomechanics",
            "Troubleshooting And Quality Control In Medical Equipments",
            "Medical Radiation Safety",
            "X-Ray Imaging And Computed Tomography",
            "Introduction To Telemedicine",
            "Developmental Biology",
            "Pearl Programming and Biopearl",
            "Industrial Microbiology",
            "Enzyme Engineering and Technology",
            "Marine Biotechnology",
            "Food Process Technology",
            "Regulation Of Gene Expression In Plants",
            "Industrial Waste Management",
            "Bioinformatics",
            "Environmental Impact Assessment",
            "Industrial Pollution Prevention And Control",
            "Environmental Engineering And Waste Management",
            "Safety And Hazard Analysis In Process Industries",
            "Waste Water Treatment",
            "Computational Logic",
            "Digital Image Processing",
            "Visualization Techniques",
            "Cellular Automata",
            "Virtual Reality",
            "Geographical Informations Systems",
            "Human Computer Interface",
            "Data Mining And Analytics",
            "Biometrics",
            "Network Programming",
            "Computer Forensics",
            "Network Routing Algorithms",
            "High Performance Computing",
            "Database Security And Privacy",
            "Natural Language Processing",
            "Service Oriented Architecture",
            "Pattern Recognition Techniques",
            "Nature Inspired Computing Techniques",
            "Optical Networks",
            "Computational Linguistics",
            "Bio Informatics",
            "Data Centric Networks",
            "Network Security",
            "Nuclear And Radiation Chemistry",
            "Advanced Analytical Methods",
            "Opto Electronics",
            "Sensors And Transducers",
            "Introduction To Multimedia Communication",
            "Digital Logic Design With Plds And Vhdl",
            "Satellite Communication And Broadcasting",
            "Cryptography And Network Security",
            "Sustainable Energy",
            "Electrical Power Utilization And Illumination",
            "Special Electrical Machines",
            "High Voltage Engineering",
            "Analytical Instrumentation",
            "Human Physiology",
            "Medical Biochemistry",
            "Plant Physiology",
            "Microbial Physiology",
            "Plant Biochemistry",
            "Medical Microbiology",
            "English for Competitive Examinations",
            "Creative Writing",
            "Indian Writing in English",
            "Science Fiction",
            "Internal Combustion Engines",
            "Alternative Sources Of Energy",
            "Production Management",
            "Solar Energy Utilization",
            "Non Traditional Machining Techniques",
            "Modern Manufacturing Techniques",
            "Automatic Control Systems",
            "Industrial Instrumentation And Control",
            "Industrial Automation",
            "Geometric Modelling",
            "Microelectro Mechanical Systems",
            "Machine Vision And Image Processing",
            "Kinematics And Dynamics Of Robots",
            "Robots Control And Programming",
            "Virtual Instrumentation",
            "Advanced Computer Vision",
            "Advanced Control Systems",
            "Applied Robotics",
            "Carbon Nanotechnology",
            "Physics Of Solid State Devices",
            "Molecular Spectroscopy And Its Applications",
            "Nanotribology",
            "Total Engineering Quality Management",
            "Marketing Management",
            "Stress Management",
            "Basics of Banking and Capital Markets",
            "Finance for Non Finance Executives",
            "Fundamental of Entrepreneurship",
            "Ethical Values for Business",
            "Information Systems for Engineers",
            "Business Environment",

            // I don't why they exist
            "NCC",
            "NSS",
            "NSO",
            "Yoga"
    };
}