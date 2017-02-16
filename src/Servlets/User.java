package Servlets;

import Authentication.*;
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.util.containers.HashMap;
import com.intellij.util.io.HttpRequests;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by stili on 12/22/2016.
 */
@WebServlet(name = "User", urlPatterns = {"/user"})
public class User extends HttpServlet {
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try{
        String id = request.getParameter("id");
        Authenticator authenticator = Authenticator.getInstance();
        AccessController accessController = AccessController.getInstance();
        Account authUser = authenticator.login(request, response);
        String uname = authUser.getUsername();
        List<Capability> caps = accessController.getCapabilites(request);
        String resourceId = "/user/" + id;
//        boolean accessGiven = accessController.checkPermission(caps, uname, resourceId, "read");
        List<Tuple> permissions = accessController.getPermission(caps, uname, resourceId);
        if(permissions.contains("friend")){
        //    HttpSession session = request.getSession();
          response.sendRedirect("friend_view.jsp");
        }
        else {
            response.sendRedirect("public_view.jsp");
        }

    }
    catch (Exception e){

    }
}


}
