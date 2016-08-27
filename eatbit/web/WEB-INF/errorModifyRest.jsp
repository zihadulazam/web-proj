<%-- 
    Document   : errorModifyRest
    Created on : Aug 20, 2016, 3:01:11 PM
    Author     : mario
--%>

<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>eatBit | error</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">

        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        
        
        <!-- icon-->
        <link rel="icon" href="img/favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    </head>
    <body>
                <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        
        <!-- Main Content -->
            
         
            <div class="container" id="main">
                    <h1>Si è verificato un errore, ci dispiace.</h1>
                    <div class="row">
                        <div class="col-md-12 update">
                            <div class="horizontally-centered">
                                <img style="margin-left:auto;margin-right:auto;" class="img-responsive" alt="Responsive image" src="img/sorry.jpg"/>
                            </div>
                            
                            <h5>Può essersi verificato un errore ci scusiamo</h5><br>
                            <p>
                                -<h3><c:out value="${errore}" /></h3><br>
                                -<h3><c:out value="${errore1}" /></h3><br>
                                -<h3><c:out value="${errore2}" /></h3><br>
                                -<h3><c:out value="${errore3}" /></h3><br>
                                -<h3><c:out value="${errore4}" /></h3><br>

                                <a class="btn btn-submit" href="${pageContext.request.contextPath}/ProfileServlet">
                                    Torna al tuo Profilo
                                </a>
                                    <hr>
                                <a class="btn btn-submit" href="${pageContext.request.contextPath}/index.jsp">
                                    Torna in Home
                                </a>
                                    <hr>
                        </div>
                    </div>
            </div>
        <!-- end Main container -->
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
    </body>
</html>
