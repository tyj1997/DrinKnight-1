package com.kwong.drinknight.home_page;

import java.sql.Time;
import java.util.Date;

/**
 * Created by 锐锋 on 2017/8/22.
 */

public class DrinkData {
    private String id;
    private String name;
    private String time;
    private String dose;
    private String date;
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDose() {
        return dose;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setDate(String date){this.date=date;}
}
