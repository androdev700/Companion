package com.androdev.timecompanion.labslot;

import java.util.ArrayList;

/**
 * Created by andro on 03/07/17.
 */

public class LabSlot {
    public ArrayList<String> course;
    public ArrayList<String> room;
    public ArrayList<String> time;

    public LabSlot() {

    }

    public LabSlot(ArrayList<String> course, ArrayList<String> room, ArrayList<String> time) {
        this.course = course;
        this.room = room;
        this.time = time;
    }

    public ArrayList<String> getCourse() {
        return course;
    }

    public ArrayList<String> getRoom() {
        return room;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setCourse(ArrayList<String> course) {
        this.course = course;
    }

    public void setRoom(ArrayList<String> room) {
        this.room = room;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

}