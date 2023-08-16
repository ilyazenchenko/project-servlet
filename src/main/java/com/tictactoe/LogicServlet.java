package com.tictactoe;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet("/logic")
public class LogicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int index = getParam(req);

        HttpSession session = req.getSession();
        Field field = extractField(session);

        if (field.checkWin() != Sign.EMPTY) {
            resp.sendRedirect("/index.jsp");
            return;
        }

        Map<Integer, Sign> map = field.getField();

        if ((map.get(index).equals(Sign.EMPTY))) {
            map.put(index, Sign.CROSS);

            if (field.checkWin() != Sign.EMPTY) {
                setAttributesAndRedirect(resp, session, field, "CROSS");
                return;
            }

            int noughtIndex = field.getEmptyFieldIndex();
            if (noughtIndex >= 0) map.put(noughtIndex, Sign.NOUGHT);

            if (field.checkWin() != Sign.EMPTY) {
                setAttributesAndRedirect(resp, session, field, "NOUGHT");
                return;
            }
        }
        setAttributesAndRedirect(resp, session, field, "NOBODY");
    }

    private static void setAttributesAndRedirect(HttpServletResponse resp, HttpSession session, Field field, String winner) throws IOException {
        session.setAttribute("winner", winner);
        session.setAttribute("field", field);
        session.setAttribute("data", field.getFieldData());
        resp.sendRedirect("/index.jsp");
    }

    private Field extractField(HttpSession session) {
        Object o = session.getAttribute("field");
        if (!o.getClass().equals(Field.class)) {
            session.invalidate();
            throw new RuntimeException("Session is BROKEN SOS!!! !!! !!!");
        }
        return (Field) o;
    }

    private int getParam(HttpServletRequest req) {
        String param = req.getParameter("click");
        boolean isNumeric = param.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(param) : 0;
    }


}
