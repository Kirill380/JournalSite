package com.liubun; /**
 * Created by Nout on 11/03/2015.
 */

import com.liubun.dao.DAORecord;
import com.liubun.dao.OracleDAORecord;
import com.liubun.model.State;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.text.ParseException;

@WebListener()
public class Listener implements ServletContextListener{

    // Public constructor is required by servlet spec
    public Listener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        DAORecord dao = new OracleDAORecord();
        State state = null;
        try {
            state  = new State();
        } catch (ParseException e) {
           throw new RuntimeException();
        }
        sce.getServletContext().setAttribute("DaoRecord", dao);
        sce.getServletContext().setAttribute("state", state);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("DaoRecord", null);
        sce.getServletContext().setAttribute("state", null);
    }


}
