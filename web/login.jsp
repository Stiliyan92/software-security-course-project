<%--
  Created by IntelliJ IDEA.
  User: stili
  Date: 10/11/2016
  Time: 00:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<form name=“loginform” action="/Login" method="POST">
    <input type=“text" size=35 name="USR">
    <input type="password" size=35 name=“PWD”>
    <input type="hidden" value=Login>
</form>
</body>
</html>
