<%--
  Created by IntelliJ IDEA.
  User: stili
  Date: 10/11/2016
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>$Title$</title>
</head>
<body>
<h1>Please log in in order to use this site!</h1>
<form name=“loginform” action="/Login" method="POST">
  <input type=“text" size=35 name="username">
  <input type="password" size=35 name="password">
  <input type="submit" value=Login>
</form>
</body>
</html>
