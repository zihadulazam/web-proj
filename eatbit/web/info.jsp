<%-- 
    Document   : info.jsp
    Created on : 14-ago-2016, 19.13.22
    Author     : 
--%>

<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        <div style='width:100%;height:100px;'></div>
        <div class="container">
            <div class="jumbotron">
                <div class="container text-center">
                    <h1>Blaa baaa</h1>
                            <div class="alert alert-success" role="alert">
                                <p>
                                    <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                            </p>
                        </div>
                    <p>
                        <button class="btn btn-primary btn-lg" onclick="goBack()">Torna Indietro</button>

                        <script>
                        function goBack() {
                            window.history.back();
                        }
                        </script>
                    </p>
                </div>
            </div>
        </div>
         <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>

</body>
</html>