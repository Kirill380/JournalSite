package com.liubun;

import com.liubun.dao.DAORecord;
import com.liubun.exception.PersistException;
import com.liubun.model.Record;
import com.liubun.model.State;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

//TODO global refactor ))
@WebServlet(name = "JournalController", value = "/content")
public class JournalController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAORecord dao = (DAORecord) getServletContext().getAttribute("DaoRecord");
        String res = request.getParameter("do");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        switch (res) {
            case "delete" :
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    dao.delete(id);
                } catch (PersistException e) {
                    e.printStackTrace();
                }
                break;
            case "update" :
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    String value = request.getParameter("value");
                    String attr = request.getParameter("attr");
                    dao.update(id, value, attr);
                } catch (PersistException e) {
                    e.printStackTrace();
                }
                break;

            case "option" :
                try {
                    State st = (State) getServletContext().getAttribute("state");

                    st.setSize(Integer.parseInt(request.getParameter("size")));
                    st.setCom(request.getParameter("sort"));
                    st.setFrom(sdf.parse(request.getParameter("from")));
                    st.setTo(sdf.parse(request.getParameter("to")));
                    int size = dao.getInterval(st.getFrom(), st.getTo()).size();
                    int add = (size % st.getSize()) != 0 ? 1 : 0;
                    getServletContext().setAttribute("pageNum", size / st.getSize() + add);

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (PersistException e) {
                    e.printStackTrace();
                }

                break;

            case "add" :
                try {
                    dao.persist(new Record());
                } catch (PersistException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAORecord dao = (DAORecord) getServletContext().getAttribute("DaoRecord");
        State st = (State) getServletContext().getAttribute("state");
        if( request.getParameter("page") != null)
            st.setPage(Integer.parseInt(request.getParameter("page")));

        List<Record> records;
        try {
            records = dao.getInterval(st.getFrom(), st.getTo());
            if(st.getSize() == 0) {
                st.setSize(5);
                int add = (records.size() % st.getSize()) != 0 ? 1 : 0;
                getServletContext().setAttribute("pageNum", records.size() / st.getSize() + add);
            }
            records.sort(st.getCom());
            List<Record> temp = cut(records);
            request.setAttribute("records", temp);
            getServletContext().setAttribute("page", st.getPage());

        } catch (PersistException e) {
            e.printStackTrace();
        }
        RequestDispatcher view =  request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    //TODO better create new dao method that will realised this logic
    // it is too laborious
    private List<Record> cut(List<Record> rec) {
        State st = (State) getServletContext().getAttribute("state");
        if(rec.size() <= st.getSize())
            return rec;
        int from = st.getSize()*(st.getPage()-1);
        int to = st.getSize()*(st.getPage());
        if(to > rec.size())
            to = rec.size();
        return rec.subList(from, to);
    }


}
