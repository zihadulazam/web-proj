<%-- 
    Document   : info.jsp
    Created on : 14-ago-2016, 19.13.22
    Author     : 
--%>

<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html>
<html>
<head>
        <title>eatBit | Info</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        
        <!-- google font link -->
        <!-- header font -->
        <!-- footer font -->
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">

        <!-- creat Restaurant liks -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    </head>
    <body>
        
        <c:set var="req" value="${pageContext.request}" />
        <c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}" />
        
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        <div style='width:100%;height:100px;'></div>
        <div class="container" style="height: 100%;min-height: 100%;">
            <div class="jumbotron">
                <div class="container text-center">
                    <h1><c:out value="${titolo}"/></h1>
                    <c:choose>
                        <c:when test="${status=='ok'}">
                            <div class="alert alert-success" role="alert">
                                <p>
                                    <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>
                        </c:when>
                        <c:when test="${status=='warning'}">
                            <div class="alert alert-warning" role="alert">
                                <p>
                                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        </c:when>
                        <c:when test="${status=='danger'}">
                            <div class="alert alert-danger" role="alert">
                                <p>
                                    <span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
                        </c:when>
                    </c:choose>
                                <c:out value="${description}"/>
                            </p>
                        </div>
                    <p>
                        
                        <c:if test="${status=='ok'}" >
                            <a class="btn btn-primary btn-lg" href="${baseURL}/ProfileServlet">Torna al Profilo</a>
                        </c:if>
                        
                        <c:if test="${status != 'ok'}" >
                            <button class="btn btn-primary btn-lg" onclick="goBack()">Torna Indietro</button>
                            <script>
                            function goBack() {
                                window.history.back();
                            }
                            </script>
                        </c:if>
                            
                        
                    </p>
                </div>
            </div>
        </div>
            </div>
        </div>
         <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>

</body>
</html>
