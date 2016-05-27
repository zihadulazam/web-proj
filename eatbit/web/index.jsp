<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="it">
    <head>
        <title>eatBit | Home</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
        <!-- eatBit css -->
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        
        <!-- google font link -->
        <link href='https://fonts.googleapis.com/css?family=Exo+2:400,800italic' rel='stylesheet' type='text/css'>
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href='http://fonts.googleapis.com/css?family=Source+Sans+Pro:200,300,400,600' rel='stylesheet' type='text/css'>
        
    </head>
    <body>
        <!-- include navbar hear -->
        <%@include file="components/navbar-second.jsp"%>
        <!-- Main Content -->
            <!-- search jumbotorn -->
                
            <div class="jumbotron">
                <div class="container-fluid">
                    <div class="horizontally-centered">
                        <div class="jumbo-text-container"> 
                            <h1 class="name"><strong>eatBit</strong></h1>
                            <div class="motto horizontally-centered">
                                <p>Trova il ristorante più <strong>adatto</strong> a te..</p> 
                            </div>
                        </div>
                        <div class="input-thumbnail thumbnail">
                            <form role="form">
                                <div class="jumbo-textbox-container form-group horizontally-centered">
                                    <input type="text" class="form-control" placeholder="Dove?">
                                    <input type="text" class="sapce-top form-control" placeholder="Nome del ristorante">
                                </div>
                                <div class="jumbo-button-container">
                                    <button class="btn btn-danger btn-lg btn-block" type="submit" aria-label="Left Align">
                                        <span class="glyphicon glyphicon-search" aria-hidden="true"></span> Cerca</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end search jumbotorn -->
            
            <!-- body after jumbotron -->
            
            <div class="container-fluid">
                <div class="info-box">
                            <h1>Example headline</h1>
                            <p class="lead">Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
                            <a class="btn btn-large btn-primary" href="#">Sign up today</a>
                </div>
            </div>
        <!-- end Main container -->
        
        <!-- include modal hear -->
        <%@include file="components/log-reg-modal.jsp"%>
        
        <!-- footer -->
        
        <!-- scripts -->
    
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="js/jquery-1.12.4.min.js"></script>
    
        <!-- js (Bootstrap) -->
        <script src="js/bootstrap.min.js"></script>
        
        <!-- login modal js -->
        <script src="js/login-register.js"></script>
        
        <!-- hosted by Microsoft Ajax CDN -->
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
    </body>
</html>
