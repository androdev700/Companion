package com.androdev.timetable.dayorder;

/**
 * Created by andro on 12/06/17.
 */

public class Order {

    public String dayOrder;

    public Order() {

    }

    public Order(String dayOrder) {
        this.dayOrder = dayOrder;
    }

    public String getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(String dayOrder) {
        this.dayOrder = dayOrder;
    }
}
