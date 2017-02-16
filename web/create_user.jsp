<%--
  Created by IntelliJ IDEA.
  User: stili
  Date: 10/23/2016
  Time: 22:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create user</title>
</head>
<body>
<body>
<% String username = (String)request.getSession().getAttribute("username");
    String password = (String)request.getSession().getAttribute("password");
%>
<h3>Hello,"<%= session.getAttribute("username") %>"</h3>
<form action="/CreateUser" method="post">
    <input type="hidden" name="username" value=<%=username%>>
    <input type="hidden" name="password" value=<%=password%>>
    <br>Username:<input type="text" name="user_to_create">
    <br>Password:<input type="password" name="user_password1">
    <br>Confirm password:<input type="password" name="user_password2">
    <br>Phone:<input type="text" name="phone">
    <br>Email:<input type="text" name="email">
    <br><input type="submit" value="CreateUser">
</form><

</body>
</body>
</html>
