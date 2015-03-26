package com.liubun.comparator;

import com.liubun.model.Record;

import java.util.Comparator;

/**
 * Created by Nout on 25/03/2015.
 */
public class CompareMsg  implements Comparator<Record> {
    @Override
    public int compare(Record o1, Record o2) {
        if(o1.getMsg() == null)
            return -1;
        if(o2.getMsg() == null)
            return 1;
        return o1.getMsg().compareTo(o2.getMsg());
    }
}
