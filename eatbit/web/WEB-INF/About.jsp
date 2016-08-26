<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="it">
    <head>
        <title>eatBit | About</title>
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
        <%@include file="components/navbar-index.jsp"%>
        
        <!-- Main Content -->
            
         
            <div class="container" id="main">
                <h1 align="justify"><strong>About EatBit</strong></h1>
    <p class="lead">EatBit è il portale di ristoranti più piccolo al mondo, probabilmente. 
    Permette agli utenti e a chi ha fame di cercare fra una manciata di ristoranti che potrebbero non esistere.
    Il marchio eatBit rappresenta la più piccola community di mangiatori al mondo, con meno di 20 utenti registrati, e il suo
    sito è attivo solo in italia.</p>
                <div class="row">
                    <div class="col-md-12">
                        <div class="horizontally-centered">
                            <img  style="margin-left:auto;margin-right:auto;" class="img-responsive" alt="Responsive image" src="img/sede.png"/>
                        </div>
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
