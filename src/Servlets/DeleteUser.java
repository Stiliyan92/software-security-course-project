package Servlets;

import Authentication.Account;
import Authentication.Authenticator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by stili on 10/23/2016.
 */
@WebServlet(name = "DeleteUser", urlPatterns = {"/DeleteUser"})
public class DeleteUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String user_to_delete = request.getParameter("user_to_delete");
        if (!user_to_delete.equals("root")) {
            Authenticator authenticator = Authenticator.getInstance();
            Account authUser = authenticator.login(request, response);
            out.println("You are" + authUser.getUsername());
            authenticator.deleteAccount(user_to_delete);
            out.println("Success!");
        }
        response.sendRedirect("home.jsp");
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
