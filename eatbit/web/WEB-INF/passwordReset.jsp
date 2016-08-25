<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="it">
    <head>
        <title>eatBit | Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- Bootstrap -->
        <link href="css/jquery-ui.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">

         <!-- single img Viewer css-->
        <link rel="stylesheet" href="css/lightbox.min.css">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        <link href="http://fonts.googleapis.com/css?family=Cookie" rel="stylesheet" type="text/css">
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-index.jsp"%>
        
        <!-- Main Content -->
            
         
            <div class="container" id="main">
                <div id="last-update">
                    <div id="info-box">
                        <form class="form-horizontal" action="${pageContext.request.contextPath}/VerifyPasswordServlet" method="post">
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
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
    </body>
</html>
