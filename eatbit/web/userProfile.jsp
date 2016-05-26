<%-- 
    Document   : userProfile
    Created on : May 21, 2016, 12:56:07 PM
    Author     : mario
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>Profile Page</title>
        
        <!-- Bootstrap -->
        <link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
        
                
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.min.js"></script>
        
    </head>
    <body>
        <h1>------------------------------------------Barra--------------------------------------</h1>
        <div class="container">
        
        <div class="col-md-3">
            <div class="row">
                <div class="col-sm-12">
                  <div class="thumbnail">
                      <img src="img/user_default.png" alt="normal user">
                    <div class="caption">
                      <hr>
                      <h3>UserName</h3>
                      <hr>
                      <p>Subscribed - </p>
                      <p>Reviews - </p>
                      <br>
                      <p>
                          <div class="btn-group">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                              More <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                              <li><a href="#">Update your info</a></li>
                              <li><a href="#">Your Restaurants</a></li>
          
                              <li role="separator" class="divider"></li>
                              <li><a href="#">Log Out</a></li>
                            </ul>
                          </div>
                    </div>
                  </div>
                </div>
            </div>    
        </div>
        
        <div class="col-md-9">
          <h2>Private Resources</h2>
          <ul class="nav nav-pills">
              <li class="active"><a data-toggle="tab" href="#home">Notifications<span class="badge">1</span></a></li>
            <li><a data-toggle="tab" href="#menu1">My Reviews<span class="badge">1</span></a></li>
            <li><a data-toggle="tab" href="#menu2">Profile Info</a></li>
          </ul>

          <div class="tab-content">
            <div id="home" class="tab-pane fade in active">
              <h3>Notifications</h3>
              
              
              
            </div>
            <div id="menu1" class="tab-pane fade">
              <h3>Reviews</h3>
              <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
            </div>
            <div id="menu2" class="tab-pane fade">
              <h3>Profile Informations</h3>
              <p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam.</p>
            </div>
          </div>
        </div>
            
        </div>

    </body>
</html>
