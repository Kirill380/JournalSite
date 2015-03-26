package com.liubun.dao;

import com.liubun.exception.PersistException;
import com.liubun.model.Record;

import java.util.Date;
import java.util.List;

/**
 * Created by Nout on 11/03/2015.
 */
public interface DAORecord {

    Record persist(Record n) throws PersistException;
    Record getByID(int id) throws PersistException;
    void delete(Record r) throws PersistException;
    void delete(int id) throws PersistException;
    void update(Record r) throws PersistException;
    void update(int id, String  value, String attr) throws PersistException;
    List<Record> getInterval(Date from, Date to) throws PersistException;

    List<Record> getAll() throws PersistException;
}
