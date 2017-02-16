<%--
  Created by IntelliJ IDEA.
  User: stili
  Date: 10/23/2016
  Time: 18:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Password</title>
</head>
<body>
<% String username = (String)request.getSession().getAttribute("username");
    String password = (String)request.getSession().getAttribute("password");


%>
<h3>Hello, ${username}</h3>
<form name=“change_password” action="/ChangePassword" method="GET">
    <input type ="hidden" name="username" value=<%= username%>>
    Old Password: <input type="password" size=35 name="password"><br>
    New Password:<input  type="password" size="35" name="pwd1"><br>
    Confirm Password: <input type="password" size="35" name="pwd2" ><br>
    <input type="submit" value=submit>
</form>
</body>
</html>
