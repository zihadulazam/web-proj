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
                        <h1 align="justify"><strong>About EatBit</strong> <br> EatBit è il portale di ristoranti più piccolo al mondo, probabilmente. 
            Permette agli utenti e a chi ha fame di cercare fra una manciata di ristoranti che potrebbero non esistere.
            Il marchio eatBit rappresenta la più piccola community di mangiatori al mondo, con meno di 20 utenti registrati, e il suo
            sito è attivo solo in italia.</h1>
                    </div>
                    <div id="updates">
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-md-12 update">
                                    <div class="horizontally-centered">
                                        <img src="img/sede.png"/>
                                    </div>
                                </div>
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
        
        <!-- Single image viewer js -->
        <script src="js/lightbox.min.js"></script>
    </body>
</html>