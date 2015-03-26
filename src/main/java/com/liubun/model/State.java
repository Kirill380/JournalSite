package com.liubun.model;

import com.liubun.comparator.CompareDate;
import com.liubun.comparator.CompareLevel;
import com.liubun.comparator.CompareMsg;
import com.liubun.comparator.CompareSource;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Nout on 25/03/2015.
 */
public class State {
    private int size;
    private int page;
    private Date to;
    private Date from;
    private String com;

    public State() throws ParseException {
        this.size = 0;
        this.page = 1;
        this.to = new Date();
        this.from = new Date(0);
        this.com = "date";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Comparator<Record> getCom() {
        return getComparator(com);
    }

    public void setCom(String com) {
        this.com = com;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    private Comparator<Record> getComparator(String name) {
        Comparator<Record> res = null;
        switch (name) {
            case "date":
                res = new CompareDate();
                break;
            case "level":
                res = new CompareLevel();
                break;
            case "msg":
                res = new CompareMsg();
                break;
            case "source":
                res = new CompareSource();
                break;
            default:

                break;
        }

        return res;
    }
}
