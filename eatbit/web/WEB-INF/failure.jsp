<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="it">
    <head>
        <title>eatBit | Failure</title>
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
            
         
            <div class="container text-center" id="main">
                <h1>L'operazione non � andata a buon fine, ci dispiace.</h1>
                <p>(Tra <strong><span id="sec">5</span></strong> secondi verrai reindirizzato alla pagina Home)</p>
                <div class="row">
                    <div class="col-md-12 update">
                        <div class="horizontally-centered">
                            <img style="margin-left:auto;margin-right:auto;" class="img-responsive" alt="Responsive image" src="img/sorry.jpg"/>
                        </div>
                    </div>
                </div>
                <br/>
                <br/>
                <div class="container">
                    <div class="row">
                        <div class="col-md-12 update">
                            <div class="horizontally-centered">
                                <p class="text-center"><a id="btn-home" class="btn btn-primary btn-lg" role="button">Home</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        <!-- end Main container -->
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>

        <!-- Redirect js -->
        <script src="js/redirect-to-home.js"></script>
    </body>
</html>
