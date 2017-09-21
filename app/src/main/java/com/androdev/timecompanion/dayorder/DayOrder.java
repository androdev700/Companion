package com.androdev.timecompanion.dayorder;

import java.util.ArrayList;

/**
 * Created by andro on 12/06/17.
 */

public class DayOrder {

    private ArrayList<String> course;

    public DayOrder() {

    }

    public DayOrder(ArrayList<String> course) {
        this.course = course;
    }

    public ArrayList<String> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }

}
