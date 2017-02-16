<%--
  Created by IntelliJ IDEA.
  User: stili
  Date: 10/11/2016
  Time: 03:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
</head>
<body>

<h3>Hi ${username}</h3>
<br>
<nav>
    <ul>

        <li><form action="change_password.jsp" method="post">
            <input type="submit" value="ChangePassword">
        </form></li>
        <% if ((boolean) session.getAttribute("userallowed")) { %>
        <li><form action="/DeleteUser" method="post">
            <input type="hidden" value=${username} name="username">
            <input type="hidden" value=${username} name="password"    >
            <br>Enter name of the user you want to delete <input type="text" name="user_to_delete">
            <input type="submit" value="DeleteUser">
        </form></li>
            <li><form action="create_user.jsp" method="post">
                <input type="submit" value="CreateUser">
            </form></li>
        <% } %>
        <li><form action="/Logout" method="post">
            <input type="hidden" name="username" value=${username}>
            <input type="hidden" name="password" value=${password}>
            <input type="submit" value="Logout">
        </form></li>
    </ul>

    <br>

    </form>
</nav>
</body>
</html>
