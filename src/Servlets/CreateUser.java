package Servlets;

import Authentication.Account;
import Authentication.Authenticator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by stili on 10/7/2016.
 */
@WebServlet(name = "CreateUser", urlPatterns = {"/CreateUser"})
public class CreateUser extends HttpServlet {

    private void refresh(HttpServletRequest request, HttpServletResponse response){
        RequestDispatcher dd = request.getRequestDispatcher("create_user.jsp");
        try {
            dd.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authenticator authenticator = Authenticator.getInstance();
        PrintWriter out = response.getWriter();
        Account authUser = authenticator.login(request, response);
        String new_user = request.getParameter("user_to_create");
        String user_pwd1 = request.getParameter("user_password1");
        String user_pwd2 = request.getParameter("user_password2");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        int returnCode = -99;
        while (returnCode != 1) {
            returnCode = authenticator.createAccount(new_user, user_pwd1, user_pwd2, phone, email);
                if (returnCode == 12) {
                    out.println("Passwords do not match.");
                    refresh(request, response);
                } else if (returnCode == 11) {
                    out.println("User with that name already exists.");
                    refresh(request, response);
                }
        }
        response.sendRedirect("home.jsp");
        out.close();
        }
    /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String name = request.getParameter("username");
        String pass = request.getParameter("password");
        Authenticator authenticator = Authenticator.getInstance();
        out.println(name + ", your password is " + pass);
        authenticator.createAccount(name, pass, pass);
        out.println("Success!");
        out.close();
    }
    */
}
