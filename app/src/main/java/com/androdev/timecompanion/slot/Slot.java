package com.androdev.timecompanion.slot;

import java.util.ArrayList;

/**
 * Created by andro on 03/07/17.
 */

public class Slot {
    private ArrayList<String> course;
    private ArrayList<String> room;

    public Slot() {

    }

    public Slot(ArrayList<String> course, ArrayList<String> room) {
        this.course = course;
        this.room = room;
    }

    public ArrayList<String> getCourse() {
        return course;
    }

    public ArrayList<String> getRoom() {
        return room;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }

    public void setRoom(ArrayList<String> room) {
        this.room = room;
    }
}
