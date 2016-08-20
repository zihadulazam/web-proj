<%-- 
    Document   : errorModifyRest
    Created on : Aug 20, 2016, 3:01:11 PM
    Author     : mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page language="java" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Errore </title>
    </head>
    <body>
        <h1>Errore durante la modifica del ristorante</h1><br>
        <p>
            -<h3><c:out value="${error}" /></h3><br>
            -<h3><c:out value="${error1}" /></h3><br>
            -<h3><c:out value="${error2}" /></h3><br>
            -<h3><c:out value="${error3}" /></h3><br>
            -<h3><c:out value="${error4}" /></h3><br>
    </body>
</html>
