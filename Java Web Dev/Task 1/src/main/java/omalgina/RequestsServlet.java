package omalgina;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@WebServlet("/servlet")

public class RequestsServlet extends HttpServlet {

    private final List<String> STATES = new ArrayList<>(Arrays.asList("IDLE", "GET BUTTON CLICKED", "POST BUTTON CLICKED",
            "PUT BUTTON CLICKED", "DELETE BUTTON CLICKED"));

    private String state = STATES.get(0);

    private void setState(String state) {
        this.state = state;
    }

    private String getState() {
        return this.state;
    }

    private int numberOfClicks;

    private void addCookies(HttpServletResponse response) {
        ++numberOfClicks;
        Cookie cookie = new Cookie("visits", String.valueOf(numberOfClicks));
        response.addCookie(cookie);
    }

    private void setResponseText(HttpServletResponse response) throws IOException {
        String responseText = "Current status of servlet: " + getState();

        addCookies(response);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseText);
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        setState(STATES.get(1));
        setResponseText(response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        setState(STATES.get(2));
        setResponseText(response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setState(STATES.get(3));
        setResponseText(response);
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setState(STATES.get(4));
        setResponseText(response);
    }

}
