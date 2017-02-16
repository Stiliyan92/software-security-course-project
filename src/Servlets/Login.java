package Servlets;

import Authentication.Account;
import Authentication.Authenticator;
import Exceptions.AuthenticationError;
import Exceptions.LockedAccount;
import Exceptions.UndefinedAccount;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by stili on 10/6/2016.
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authenticator authenticator = Authenticator.getInstance();
        String aname = request.getParameter("username");
        String apwd = request.getParameter("password");
        HttpSession session = request.getSession(true);
        Account authUser = null;
        try {
            authUser = authenticator.login(aname, apwd);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Unknown username/password, try again"); // Set error message.
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
        if (authUser != null) {
            session.setAttribute("username", aname);
            session.setAttribute("password", apwd);
            if (aname.equals("root")){
                session.setAttribute("userallowed", true);
            }
            else{
                session.setAttribute("userallowed", false);
            }
            //Cookie loginCookie = new Cookie(dd"username", aname);
            //setting cookie to expiry in 30 mins
 //           loginCookie.setMaxAge(30*60);
  //          response.addCookie(loginCookie);
            response.sendRedirect("home.jsp"); // Redirect to home/succes page.
        }else {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Either user name or password is wrong.</font>");
            rd.include(request, response);
        }
    }
/*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    */
}
