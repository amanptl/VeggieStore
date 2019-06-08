package com.amanpatel.veggiestoretest0.Models;

public class Holiday {
    private String id;
    private String title;
    private String holidaydate;
    private String dealerid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHolidaydate() {
        return holidaydate;
    }

    public void setHolidaydate(String holidaydate) {
        this.holidaydate = holidaydate;
    }

    public String getDealerid() {
        return dealerid;
    }

    public void setDealerid(String dealerid) {
        this.dealerid = dealerid;
    }

    public Holiday(String id, String title, String holidaydate, String dealerid) {
        this.id = id;
        this.title = title;
        this.holidaydate = holidaydate;
        this.dealerid = dealerid;
    }
}
