package com.liubun.dao;

import com.liubun.exception.PersistException;
import com.liubun.model.Record;

import java.sql.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nout on 11/03/2015.
 */
public class OracleDAORecord implements DAORecord {
//   private DataSource ds;
    private String user = "journal";
    private String password = "send08";
    private String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
    private String driver = "oracle.jdbc.driver.OracleDriver";

    public OracleDAORecord() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        Hashtable env = new Hashtable();
//        env.put(Context.INITIAL_CONTEXT_FACTORY,
//                "com.sun.jndi.ldap.LdapCtxFactory");
//        env.put(Context.PROVIDER_URL, "ldap://ldap.wiz.com:389");
//        env.put(Context.SECURITY_PRINCIPAL, "joeuser");
//        env.put(Context.SECURITY_CREDENTIALS, "joepassword");

//        try {
//            Context ic = new InitialContext(env);
//            ds = (DataSource) ic.lookup("java:comp/env/jdbc/oracle");
//
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }

    }


    @Override
    public Record persist(Record r) throws PersistException {
        Connection con = null;
        int auto_id = 0;
        String generatedColumns[] = { "ID" };
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("insert into record(id, date_appear, levell, source, message) " +
                                                        "values(gen_id.nextval, ?, ?, ?, ?)", generatedColumns);

            ps.setDate(1, new java.sql.Date(r.getDate().getTime()));
            ps.setString(2, r.getLevel());
            ps.setString(3, r.getSource());
            ps.setString(4, r.getMsg());

            int count = ps.executeUpdate();
            if (count != 1) {
                throw new PersistException("On persist inserted more then 1 record: " + count);
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            auto_id = rs.getInt(1);
            con.commit();

            return getByID(auto_id);
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        finally {
            if(con != null)  realiseRes(con);

        }
    }

    @Override
    public Record getByID(int id) throws PersistException {
        List<Record> list;
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("select id, date_appear, levell, source, message from record where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            list = parseResultSet(rs);
            if (list == null || list.size() == 0) {
                throw new PersistException("Record with PK = " + id + " not found.");
            }
            if (list.size() > 1) {
                throw new PersistException("Received more than one record.");
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        finally {
            if(con != null)  realiseRes(con);

        }
        return list.iterator().next();
    }

    @Override
    public void delete(Record r)  throws PersistException {
        delete(r.getId());
    }

    @Override
    public void delete(int id) throws PersistException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("delete from record where id = ?");
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            if (count != 1) {
                throw new PersistException("On delete modify more then 1 record: " + count);
            }
            con.commit();
        } catch (SQLException e) {
            throw new PersistException(e);
        } finally {
            if(con != null)
                realiseRes(con);

        }
    }

    @Override
    public void update(Record r) throws PersistException {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("update record set " +
                                                        "date_appear = ?, levell = ?, source = ?, message = ? where id = ?");
            ps.setDate(1, new java.sql.Date(r.getDate().getTime()));
            ps.setString(2, r.getLevel());
            ps.setString(3, r.getSource());
            ps.setString(4, r.getMsg());
            ps.setInt(5, r.getId());
            int count = ps.executeUpdate();
            if (count != 1) {
                throw new PersistException("On update modify more then 1 record: " + count);
            }
            con.commit();
        } catch (SQLException e) {
            throw new PersistException(e);
        } finally {
            if(con != null)
                realiseRes(con);

        }
    }

    // workaround??
    @Override
    public void update(int id, String value, String attr) throws PersistException {
        Record r = getByID(id);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        switch (attr) {
            case "date" :
                try {
                    r.setDate(sdf.parse(value));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "level" :
                r.setLevel(value);
                break;
            case "source" :
                r.setSource(value);
                break;
            case "msg" :
                r.setMsg(value);
                break;
            default:
                throw new PersistException("No such field in record");
        }
        update(r);
    }

    @Override
    public List<Record> getInterval(Date from, Date to) throws PersistException {
        List<Record> list;
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("select id, date_appear, levell, source, message from record " +
                                                        "where date_appear > ? and date_appear < ?");

            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            ResultSet rs = ps.executeQuery();
            list = parseResultSet(rs);

        } catch (SQLException e) {
            throw new PersistException(e);
        }
        finally {
            if(con != null)  realiseRes(con);

        }
        return list;
    }

    @Override
    public List<Record> getAll() throws PersistException {
        List<Record> list;
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement("select id, date_appear, levell, source, message from record");
            ResultSet rs = ps.executeQuery();
            list = parseResultSet(rs);

        } catch (SQLException e) {
            throw new PersistException(e);
        }
        finally {
            if(con != null)  realiseRes(con);

        }
        return list;
    }


    private Connection getConnection() throws PersistException {
        Locale.setDefault(Locale.ENGLISH); // work around
        Connection con = null;
        try {
//             con = ds.getConnection();
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
            return con;
        } catch (SQLException e) {
            throw new PersistException("Cannot obtain connection", e);
        }

    }

    private  void realiseRes(Connection con) throws PersistException {
        try {
            con.close();
        } catch (SQLException e) {
            throw new PersistException("Cannot realise connection", e);
        }

    }

    private List<Record> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Record> res = new LinkedList<Record>();
        try {
            while(rs.next()){
                Record temp = new Record(
                        rs.getDate("date_appear"),
                        rs.getInt("id"),
                        rs.getString("source"),
                        rs.getString("levell"),
                        rs.getString("message")
                );
                res.add(temp);
            }
        } catch (SQLException e) {
            throw new PersistException(e);
        }
        return res;
    }

}
