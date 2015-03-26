package com.liubun.model;

import java.util.Date;

/**
 * Created by Nout on 15/03/2015.
 */
public class Record {
    private int id;
    private Date date = new Date();
    private String level = "";
    private String source = "";
    private String msg = "";

    public Record() {

    }


    public Record(Date date, int id, String source, String level, String msg) {
        this.date = date;
        this.id = id;
        this.source = source;
        this.level = level;
        this.msg = msg;
    }


    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + date +
                ", level='" + level + '\'' +
                ", source='" + source + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
