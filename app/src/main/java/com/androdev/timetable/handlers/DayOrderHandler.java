package com.androdev.timetable.handlers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by andro on 08/06/17.
 */

public class DayOrderHandler {

    private static final String TAG = "DayOrderHandler";
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference dayReference;

    public DatabaseReference initDatabase(String date) {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("CheckOrder");
        dayReference = databaseReference.child(date);
        return dayReference;
    }

}
