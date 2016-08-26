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
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        <title>Errore </title>
    </head>
    <body>
        <h1>Errore durante la modifica del ristorante</h1><br>
        <p>
            -<h3><c:out value="${errore}" /></h3><br>
            -<h3><c:out value="${errore1}" /></h3><br>
            -<h3><c:out value="${errore2}" /></h3><br>
            -<h3><c:out value="${errore3}" /></h3><br>
            -<h3><c:out value="${errore4}" /></h3><br>
            
            <a class="btn btn-submit" href="${pageContext.request.contextPath}/ProfileServlet">
                Torna Indietro
            </a>
    </body>
</html>
