package com.liubun.comparator;

import com.liubun.model.Record;

import java.util.Comparator;

public class CompareDate implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
