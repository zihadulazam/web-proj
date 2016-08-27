<%@ page language="java" session="true" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="error.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="it">
    <head>
        <title>eatBit | Recupera Password</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- AutoComplete -->
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
                <div id="last-update">
                    <div id="info-box">
                        <form class="form-horizontal" action="${pageContext.request.contextPath}/VerifyPasswordServlet" method="post" id="reset-form">
                    <fieldset>

                    <!-- Form Name -->
                    <legend>Password reset</legend>

                    <!-- Password input-->
                    <div class="form-group">
                      <label class="col-md-4 control-label" for="password">La tua nuova password</label>
                      <div class="col-md-4">
                        <input id="password" name="password" placeholder="password" class="form-control input-md" required="" type="password">

                      </div>
                    </div>

                    <!-- Password input-->
                    <div class="form-group">
                      <label class="col-md-4 control-label" for="passwordCheck">Scrivila di nuovo per sicurezza</label>
                      <div class="col-md-4">
                        <input id="passwordCheck" name="passwordCheck" placeholder="password" class="form-control input-md" required="" type="password">

                      </div>
                    </div>

                    <!-- Button -->
                    <div class="form-group">
                      <label class="col-md-4 control-label" for=""></label>
                      <div class="col-md-4">
                        <button type="submit" class="btn btn-primary">Invia</button>
                      </div>
                    </div>
                    <!-- input nascosti presi da parametri, da mandare per verifica del token dell'utente -->
                    <input type="hidden" name="id" value="${param.id}" />
                    <input type="hidden" name="token" value="${param.token}"/>
                    </fieldset>
                    </form>

                    </div>
                </div>
            </div>
        <!-- end Main container -->
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        <%@include file="components/footer.html"%>
        
        <!-- PasswordReset Check js -->
        <script src="js/passwordReset.js"></script>
    </body>
</html>
