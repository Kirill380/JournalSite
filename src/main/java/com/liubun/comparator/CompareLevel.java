package com.liubun.comparator;

import com.liubun.model.Record;

import java.util.Comparator;

/**
 * Created by Nout on 25/03/2015.
 */
public class CompareLevel implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        if(o1.getLevel() == null)
            return -1;
        if(o2.getLevel() == null)
            return 1;
        return o1.getLevel().compareTo(o2.getLevel());
    }
}
