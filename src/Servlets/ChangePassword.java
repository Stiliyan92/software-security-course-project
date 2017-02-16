package Servlets;

import Authentication.Account;
import Authentication.Authenticator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by stili on 10/23/2016.
 */
@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        out.println(session.getId());
        String pwd1 = request.getParameter("pwd1");
        String pwd2 = request.getParameter("pwd2");
        Authenticator authenticator = Authenticator.getInstance();
        Account acc = authenticator.login(request, response);
        if (acc != null) {
            authenticator.changePassword(acc.getUsername(), pwd1, pwd2);
        }
        response.sendRedirect("home.jsp"); // Redirect to home/succes page.
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
